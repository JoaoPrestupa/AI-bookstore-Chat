package com.bookstore.api.controllers;

import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.openai.OpenAiChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/bookstore")
public class BookstoreAssistantController {

    @Autowired
    private OpenAiChatClient chatClient;

    @GetMapping("/informations")
    public String bookStoreChat(@RequestParam(value = "message", defaultValue = "Quais são os livros best sellers dos últimos anos ?") String message){
        return chatClient.call(message);
    }

    @GetMapping("/informations/prompt")
    public ChatResponse bookStoreChatPrompt(@RequestParam(value = "message", defaultValue = "Quais são os livros best sellers dos últimos anos ?") String message){
        return chatClient.call(new Prompt(message));
    }

    @GetMapping("/reviews")
    public String bookStoreReview(@RequestParam(value = "book", defaultValue = "Dom Quixote") String book){
        PromptTemplate promptTemplate = new PromptTemplate("""
                Por favor, me forneça um breve resumo do livro {book} e também a biografia do seu autor.""");
        promptTemplate.add("book", book);
        return this.chatClient.call(promptTemplate.create()).getResult().getOutput().getContent();
    }

    @GetMapping("/stream/informations")
    public Flux<String> bookStoreChatStream(@RequestParam(value = "message", defaultValue = "Quais são os livros best sellers dos últimos anos ?") String message){
        return chatClient.stream(message);
    }

    @GetMapping("/stream/prompt/informations")
    public Flux<ChatResponse> bookStoreChatStreamPrompt(@RequestParam(value = "message", defaultValue = "Quais são os livros best sellers dos últimos anos ?") String message){
        return chatClient.stream(new Prompt(message));
    }

}
