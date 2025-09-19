package com.crane.answer.controller;

import com.crane.answer.common.R;
import com.crane.answer.model.po.ForumBoard;
import com.crane.answer.model.vo.ForumBoardResp;
import com.crane.answer.service.ForumBoardService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author crane
 * @date 2025.09.19 上午12:11
 * @description
 **/
@RestController
@RequestMapping("/forum/board")
public class ForumBoardController {

    @Resource
    private ForumBoardService forumBoardService;

    @GetMapping("/list")
    public R<List<ForumBoard>> listForumBoard() {
        List<ForumBoard> list = forumBoardService.getForumBoardTree(null);
        return R.ok(list);
    }
}
