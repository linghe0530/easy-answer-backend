package com.crane.answer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crane.answer.model.po.UserMessage;
import com.crane.answer.service.UserMessageService;
import com.crane.answer.mapper.UserMessageMapper;
import org.springframework.stereotype.Service;

/**
* @author linghe01
* @description 针对表【user_message(用户消息)】的数据库操作Service实现
* @createDate 2025-09-19 00:53:03
*/
@Service
public class UserMessageServiceImpl extends ServiceImpl<UserMessageMapper, UserMessage>
    implements UserMessageService{

}




