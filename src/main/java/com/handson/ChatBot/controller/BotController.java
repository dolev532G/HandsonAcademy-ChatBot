package com.handson.ChatBot.controller;

import com.handson.ChatBot.service.ChuckNorrisService;
import com.handson.ChatBot.service.MoviesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Service
@RestController
@RequestMapping("/bot")
public class BotController {

    @Autowired
    MoviesService providerService;
    @Autowired
    ChuckNorrisService chuckNorrisService;

    @RequestMapping(value = "/imdb", method = RequestMethod.GET)
    public ResponseEntity<?> getMovies(@RequestParam String keyword) throws IOException {
        return new ResponseEntity<>(providerService.searchProducts(keyword), HttpStatus.OK);
    }

    @RequestMapping(value = "/ChuckNorris", method = RequestMethod.GET)
    public ResponseEntity<?> getChuckNorris(@RequestParam String category) throws IOException {
        return new ResponseEntity<>(chuckNorrisService.searchCategory(category), HttpStatus.OK);
    }
}