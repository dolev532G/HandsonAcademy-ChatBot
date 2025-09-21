package com.handson.ChatBot.controller;

import com.handson.ChatBot.service.ChuckNorrisService;
import com.handson.ChatBot.service.MoviesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;

@Service
@RestController
@RequestMapping("/bot")
public class BotController {

    @Autowired
    MoviesService moviesService;
    @Autowired
    ChuckNorrisService chuckNorrisService;

    @RequestMapping(value = "/imdb", method = RequestMethod.GET)
    public ResponseEntity<?> getMovies(@RequestParam String keyword) throws IOException {
        return new ResponseEntity<>(moviesService.searchProducts(keyword), HttpStatus.OK);
    }

    @RequestMapping(value = "/ChuckNorris", method = RequestMethod.GET)
    public ResponseEntity<?> getChuckNorris(@RequestParam String category) throws IOException {
        return new ResponseEntity<>(chuckNorrisService.searchCategory(category), HttpStatus.OK);
    }


    @RequestMapping(value = "", method = { RequestMethod.POST})
    public ResponseEntity<?> getBotResponse(@RequestBody BotQuery query) throws IOException {
        HashMap<String, String> params = query.getQueryResult().getParameters();
        String res = "Not found";
        if (params.containsKey("keyword")) {
            res = moviesService.searchProducts(params.get("keyword"));

        }
        return new ResponseEntity<>(BotResponse.of(res), HttpStatus.OK);
    }


    static class BotQuery {
        QueryResult queryResult;

        public QueryResult getQueryResult() {
            return queryResult;
        }
    }

    static class QueryResult {
        HashMap<String, String> parameters;

        public HashMap<String, String> getParameters() {
            return parameters;
        }
    }

    static class BotResponse {
        String fulfillmentText;
        String source = "BOT";

        public String getFulfillmentText() {
            return fulfillmentText;
        }

        public String getSource() {
            return source;
        }

        public static BotResponse of(String fulfillmentText) {
            BotResponse res = new BotResponse();
            res.fulfillmentText = fulfillmentText;
            return res;
        }
    }
}