package com.handson.ChatBot.service;


import org.springframework.stereotype.Service;

@Service
public class ProviderService {

    public String searchProducts(String keyword) {
        return "Searched for:" + keyword;
    }
}