package com.handson.ChatBot.service;



import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;
import okhttp3.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import okhttp3.Response;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.regex.Matcher;

@Service
public class ChuckNorrisService {

    // Class to map JSON response
    public static class Joke {
        private String id;
        private String value;
        private java.util.List<String> categories;

        // Getters and setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        public String getValue() { return value; }
        public void setValue(String value) { this.value = value; }

        public java.util.List<String> getCategories() { return categories; }
        public void setCategories(java.util.List<String> categories) { this.categories = categories; }
    }


    public String searchCategory(String category) throws IOException {
        return getJokeByCategory(category);
    }


    public String getJokeByCategory(String category) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.chucknorris.io/jokes/random?category=" + category)
                .get()
                .addHeader("Accept", "application/json")
                .addHeader("User-Agent", "Java OkHttp Client")
                .build();

        Response response = client.newCall(request).execute();

        return parseJockJSON(response.body().string());
    }


    private String parseJockJSON(String JSON_String) throws IOException {
        ObjectMapper om = new ObjectMapper();

        JsonNode root = om.readTree(JSON_String);

        // Category name (use "uncategorized" if empty)
        JsonNode categoriesNode = root.path("categories");
        String categoryName = (categoriesNode.isArray() && !categoriesNode.isEmpty())
                ? categoriesNode.get(0).asText()
                : "uncategorized";

        // Joke ID (first 5 letters)
        String jokeId = root.path("id").asText();
        if (jokeId.length() > 5) jokeId = jokeId.substring(0, 5);

        // Joke value
        String value = root.path("value").asText();

        // Return formatted string
        return "Category: " + categoryName + "\n" +
                "JokeID: " + jokeId + "\n" +
                "Value: " + value;
    }

}







