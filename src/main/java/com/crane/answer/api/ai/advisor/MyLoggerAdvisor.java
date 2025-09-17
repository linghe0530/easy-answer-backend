package com.crane.answer.api.ai.advisor;//package com.crane.answer.api.ai.advisor;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.ai.chat.client.ChatClientRequest;
//import org.springframework.ai.chat.client.ChatClientResponse;
//import org.springframework.ai.chat.client.advisor.api.AdvisorChain;
//import org.springframework.ai.chat.client.advisor.api.BaseAdvisor;
//import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
//import org.springframework.ai.chat.client.advisor.api.StreamAdvisorChain;
//import reactor.core.publisher.Flux;
//
///**
// * @author crane
// * @date 2025.09.13 上午1:50
// * @description
// **/
//@Slf4j
//public class MyLoggerAdvisor implements BaseAdvisor {
//    @Override
//    public Flux<ChatClientResponse> adviseStream(ChatClientRequest chatClientRequest, StreamAdvisorChain streamAdvisorChain) {
//        return BaseAdvisor.super.adviseStream(chatClientRequest, streamAdvisorChain);
//    }
//
//    @Override
//    public ChatClientResponse adviseCall(ChatClientRequest chatClientRequest, CallAdvisorChain callAdvisorChain) {
//        return BaseAdvisor.super.adviseCall(chatClientRequest, callAdvisorChain);
//    }
//
//    @Override
//    public String getName() {
//        return BaseAdvisor.super.getName();
//    }
//
//    @Override
//    public ChatClientRequest before(ChatClientRequest chatClientRequest, AdvisorChain advisorChain) {
//        return null;
//    }
//
//    @Override
//    public ChatClientResponse after(ChatClientResponse chatClientResponse, AdvisorChain advisorChain) {
//        return null;
//    }
//
//    @Override
//    public int getOrder() {
//        return 0;
//    }
//}
