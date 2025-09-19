package com.crane.answer.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crane.answer.exception.BusinessException;
import com.crane.answer.exception.ErrorCode;
import com.crane.answer.model.dto.interview.InterviewQueryRequest;
import com.crane.answer.model.dto.interviewBank.InterviewBankAddRequest;
import com.crane.answer.model.dto.interviewBank.InterviewBankQueryRequest;
import com.crane.answer.model.po.InterviewBank;
import com.crane.answer.model.po.User;
import com.crane.answer.model.vo.InterviewBankResp;
import com.crane.answer.model.vo.InterviewResp;
import com.crane.answer.model.vo.UserResp;
import com.crane.answer.service.InterviewBankService;
import com.crane.answer.mapper.InterviewBankMapper;
import com.crane.answer.service.InterviewService;
import com.crane.answer.service.UserService;
import com.crane.answer.utils.StringUtils;
import com.crane.answer.utils.ThrowUtils;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author crane
 * @description 针对表【interview_question_bank(题库)】的数据库操作Service实现
 * @createDate 2025-09-18 09:45:28
 */
@Service
public class InterviewBankServiceImpl extends ServiceImpl<InterviewBankMapper, InterviewBank>
        implements InterviewBankService {
    @Resource
    private UserService userService;
    @Resource
    private InterviewService interviewService;
    @Override
    public InterviewBankResp getInterviewBankResp(InterviewBank bank) {
        // 对象转封装类
        InterviewBankResp resp = InterviewBankResp.objToVo(bank);

        Long userId = bank.getUserId();
        User user = null;
        if (userId != null && userId > 0) {
            user = userService.getById(userId);
        }
        UserResp userResp = userService.getUserResp(user);
        resp.setUser(userResp);
        return resp;
    }

    @Override
    public Long addInterviewBank(InterviewBankAddRequest request) {
        InterviewBank questionBank = new InterviewBank();
        BeanUtils.copyProperties(request, questionBank);
        UserResp loginUser = userService.getLoginInfo();
        questionBank.setUserId(loginUser.getId());
        // 写入数据库
        boolean result = this.save(questionBank);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "添加失败");
        }
        return questionBank.getId();
    }

    @Override
    public void deleteInterviewBank(Long id) {
        UserResp loginUser = userService.getLoginInfo();
        // 判断是否存在
        InterviewBank oldQuestionBank = this.getById(id);
        if (oldQuestionBank == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "题库不存在");
        }
        // 仅本人或管理员可删除
        if (!oldQuestionBank.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 操作数据库
        boolean result = this.removeById(id);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
    }

    @Override
    public Page<InterviewBankResp> getInterviewBankRespPage(InterviewBankQueryRequest request) {
        int current = request.getCurrent();
        int pageSize = request.getPageSize();

        // 查询数据库
        Page<InterviewBank> page = this.page(new Page<>(current, pageSize),
                this.getQueryWrapper(request));

        Page<InterviewBankResp> pageResp = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        List<InterviewBank> records = page.getRecords();
        if (CollUtil.isEmpty(records)) {
            return pageResp;
        }
        // 对象列表 => 封装对象列表
        List<InterviewBankResp> questionBankVOList = records.stream().map(InterviewBankResp::objToVo).collect(Collectors.toList());

        // 1. 关联查询用户信息
        Set<Long> userIdSet = records.stream().map(InterviewBank::getUserId).collect(Collectors.toSet());
        Map<Long, User> userIdUserListMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.toMap(User::getId, v -> v));
        // 填充信息
        questionBankVOList.forEach(questionBankVO -> {
            Long userId = questionBankVO.getUserId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId);
            }
            questionBankVO.setUser(userService.getUserResp(user));
        });

        pageResp.setRecords(questionBankVOList);
        return pageResp;
    }

    @Override
    public InterviewBankResp getInterviewBankRespById(InterviewBankQueryRequest request) {
        Long id = request.getId();
        // 查询数据库
        InterviewBank questionBank = this.getById(id);
        ThrowUtils.throwIf(questionBank == null, ErrorCode.NOT_FOUND_ERROR);
        // 查询题库封装类
        InterviewBankResp resp = this.getInterviewBankResp(questionBank);
        // 是否要关联查询题库下的题目列表
        boolean needQuery = request.isNeedQueryInterviewList();
        if (needQuery) {
            InterviewQueryRequest interviewQueryRequest = new InterviewQueryRequest();
            interviewQueryRequest.setInterviewBankId(id);
            // 可以按需支持更多的题目搜索参数，比如分页
            interviewQueryRequest.setPageSize(request.getPageSize());
            interviewQueryRequest.setCurrent(request.getCurrent());
            Page<InterviewResp> page = interviewService.getInterviewRespPage(interviewQueryRequest);
            resp.setInterviewPage(page);
        }
        return resp;
    }

    @Override
    public Wrapper<InterviewBank> getQueryWrapper(InterviewBankQueryRequest questionBankQueryRequest) {
        QueryWrapper<InterviewBank> queryWrapper = new QueryWrapper<>();
        if (questionBankQueryRequest == null) {
            return queryWrapper;
        }
        // todo 从对象中取值
        Long id = questionBankQueryRequest.getId();
        Long notId = questionBankQueryRequest.getNotId();
        String title = questionBankQueryRequest.getTitle();
        String searchText = questionBankQueryRequest.getSearchText();
        Long userId = questionBankQueryRequest.getUserId();
        String description = questionBankQueryRequest.getDescription();
        String picture = questionBankQueryRequest.getPicture();

        // todo 补充需要的查询条件
        // 从多字段中搜索
        if (StringUtils.isNotBlank(searchText)) {
            // 需要拼接查询条件
            queryWrapper.and(qw -> qw
                    .like("title", searchText)
                    .or()
                    .like("description", searchText)
            );
        }
        // 模糊查询
        queryWrapper.like(StringUtils.isNotBlank(title), "title", title);
        queryWrapper.like(StringUtils.isNotBlank(description), "description", description);
        // 精确查询
        queryWrapper.ne(ObjectUtils.isNotEmpty(notId), "id", notId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(picture), "picture", picture);
        return queryWrapper;
    }


}




