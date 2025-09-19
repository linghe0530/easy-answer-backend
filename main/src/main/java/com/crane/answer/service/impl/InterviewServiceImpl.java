package com.crane.answer.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crane.answer.model.dto.interview.InterviewQueryRequest;
import com.crane.answer.model.po.Interview;
import com.crane.answer.model.po.InterviewBankQuestion;
import com.crane.answer.model.po.User;
import com.crane.answer.model.vo.InterviewResp;
import com.crane.answer.service.InterviewBankQuestionService;
import com.crane.answer.service.InterviewService;
import com.crane.answer.mapper.InterviewMapper;
import com.crane.answer.service.UserService;
import com.crane.answer.utils.StringUtils;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author crane
 * @description 针对表【interview_question(题目)】的数据库操作Service实现
 * @createDate 2025-09-18 09:45:28
 */
@Service
public class InterviewServiceImpl extends ServiceImpl<InterviewMapper, Interview>
        implements InterviewService {
    @Resource
    private UserService userService;
    @Resource
    private InterviewBankQuestionService interviewBankQuestionService;

    @Override
    public LambdaQueryWrapper<Interview> getQueryWrapper(InterviewQueryRequest request) {
        LambdaQueryWrapper<Interview> queryWrapper = new LambdaQueryWrapper<>();
        if (request == null) {
            return queryWrapper;
        }

        Long id = request.getId();
        Long notId = request.getNotId();
        String searchText = request.getSearchText();
        String title = request.getTitle();
        String content = request.getContent();
        List<String> tags = request.getTags();
        String answer = request.getAnswer();
        Long questionBankId = request.getInterviewBankId();
        Long userId = request.getUserId();
        int current = request.getCurrent();
        int pageSize = request.getPageSize();
        List<String> tagList = request.getTags();

        if (StringUtils.isNotBlank(searchText)) {
            queryWrapper.and(qw -> qw
                    .like(Interview::getTitle, searchText)  // 替换 "title" 为 Lambda 方法引用
                    .or()
                    .like(Interview::getContent, searchText) // 替换 "content" 为 Lambda 方法引用
            );
        }

        // 3. 单字段模糊查询（标题/内容/答案）
        queryWrapper.like(StringUtils.isNotBlank(title), Interview::getTitle, title);
        queryWrapper.like(StringUtils.isNotBlank(content), Interview::getContent, content);
        queryWrapper.like(StringUtils.isNotBlank(answer), Interview::getAnswer, answer);

        // 4. JSON数组标签查询（保留原逻辑：匹配数组中的单个标签，需用双引号包裹避免部分匹配）
        if (CollUtil.isNotEmpty(tagList)) {
            for (String tag : tagList) {
                // 字段 tags 对应实体类的 getTags() 方法，值仍需拼接双引号（与原SQL逻辑一致）
                queryWrapper.like(Interview::getTags, "\"" + tag + "\"");
            }
        }

        // 5. 精确查询（ID不等于/等于、用户ID等于）
        queryWrapper.ne(ObjectUtils.isNotEmpty(notId), Interview::getId, notId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), Interview::getId, id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), Interview::getUserId, userId);


        return queryWrapper;
    }

    public Page<Interview> listQuestionByPage(InterviewQueryRequest request) {
        long current = request.getCurrent();
        long size = request.getPageSize();
        // 题目表的查询条件
        LambdaQueryWrapper<Interview> queryWrapper = this.getQueryWrapper(request);
        // 根据题库查询题目列表接口
        Long bankId = request.getInterviewBankId();
        if (bankId != null) {
            // 查询题库内的题目 id
            LambdaQueryWrapper<InterviewBankQuestion> lambdaQueryWrapper = Wrappers.lambdaQuery(InterviewBankQuestion.class)
                    .select(InterviewBankQuestion::getInterviewId)
                    .eq(InterviewBankQuestion::getInterviewBankId, bankId);
            List<InterviewBankQuestion> interviewList = interviewBankQuestionService.list(lambdaQueryWrapper);


            if (CollUtil.isNotEmpty(interviewList)) {
                // 取出题目 id 集合
                Set<Long> interviewIdSet = interviewList.stream()
                        .map(InterviewBankQuestion::getInterviewId)
                        .collect(Collectors.toSet());
                // 复用原有题目表的查询条件
                queryWrapper.in(Interview::getId, interviewIdSet);
            } else {
                // 题库为空，则返回空列表
                return new Page<>(current, size, 0);
            }
        }
        // 查询数据库
        return this.page(new Page<>(current, size), queryWrapper);
    }

    @Override
    public Page<InterviewResp> getInterviewRespPage(InterviewQueryRequest request) {
        Page<Interview> page = listQuestionByPage(request);
        List<Interview> questionList = page.getRecords();
        Page<InterviewResp> pageResp = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        if (CollUtil.isEmpty(questionList)) {
            return pageResp;
        }
        // 对象列表 => 封装对象列表
        List<InterviewResp> questionVOList = questionList.stream().map(InterviewResp::objToVo).collect(Collectors.toList());

        // 1. 关联查询用户信息
        Set<Long> userIdSet = questionList.stream().map(Interview::getUserId).collect(Collectors.toSet());
        Map<Long, User> userIdUserListMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.toMap(User::getId, c -> c));
        // 填充信息
        questionVOList.forEach(questionVO -> {
            Long userId = questionVO.getUserId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId);
            }
            questionVO.setUser(userService.getUserResp(user));
        });

        pageResp.setRecords(questionVOList);
        return pageResp;
    }
}




