package com.crane.answer.manager.ai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

/**
 * @author crane
 * @date 2025.09.13 下午12:21
 * @description
 **/
@Component
public class AiManager {


    private final ChatClient dashScopeChatClient;
    public static final String SYSTEM_MESSAGE = "";

    public AiManager(ChatClient.Builder chatClientBuilder) {
        this.dashScopeChatClient = chatClientBuilder
                .build();
    }


    public String doRequest(String systemMessage, String userMessage) {
        return dashScopeChatClient.prompt().system(systemMessage).user(userMessage)
                .call()
                .content();
    }

    public Flux<String> doRequestStream(String systemMessage, String userMessage) {
        return dashScopeChatClient.prompt().system(systemMessage).user(userMessage)
                .stream().content();
    }

    public String doRequest(String userMessage) {
        return dashScopeChatClient.prompt().system(SYSTEM_MESSAGE).user(userMessage)
                .call()
                .content();
    }
}
