package com.crane.answer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crane.answer.model.po.ForumBoard;
import com.crane.answer.model.vo.ForumBoardResp;
import com.crane.answer.service.ForumBoardService;
import com.crane.answer.mapper.ForumBoardMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author linghe01
 * @description 针对表【forum_board(文章板块信息)】的数据库操作Service实现
 * @createDate 2025-09-18 23:51:11
 */
@Service
public class ForumBoardServiceImpl extends ServiceImpl<ForumBoardMapper, ForumBoard>
        implements ForumBoardService {

    @Override
    public List<ForumBoard> getForumBoardTree(Integer postType) {

        List<ForumBoard> list = this.lambdaQuery()
                .eq(postType != null, ForumBoard::getPostType, postType)
                .orderByAsc(ForumBoard::getSort)
                .list();

        return buildBoardTree(list, 0L);
    }

    private List<ForumBoard> buildBoardTree(List<ForumBoard> list, Long parentId) {
        ArrayList<ForumBoard> boardRespList = new ArrayList<>();
        for (ForumBoard forumBoard : list) {
            if (forumBoard.getParentId().equals(parentId)) {
                forumBoard.setChildren(buildBoardTree(list, forumBoard.getId()));
                boardRespList.add(forumBoard);
            }
        }
        return boardRespList;
    }
}




