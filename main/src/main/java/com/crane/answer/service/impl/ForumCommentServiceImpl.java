package com.crane.answer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crane.answer.model.po.ForumComment;
import com.crane.answer.service.ForumCommentService;
import com.crane.answer.mapper.ForumCommentMapper;
import org.springframework.stereotype.Service;

/**
* @author linghe01
* @description 针对表【forum_comment(评论)】的数据库操作Service实现
* @createDate 2025-09-19 00:53:03
*/
@Service
public class ForumCommentServiceImpl extends ServiceImpl<ForumCommentMapper, ForumComment>
    implements ForumCommentService{

}




