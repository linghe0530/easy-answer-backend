package com.crane.answer.service;

import com.crane.answer.model.po.ForumBoard;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author linghe01
* @description 针对表【forum_board(文章板块信息)】的数据库操作Service
* @createDate 2025-09-18 23:51:11
*/
public interface ForumBoardService extends IService<ForumBoard> {

    List<ForumBoard> getForumBoardTree(Integer postType);
}
