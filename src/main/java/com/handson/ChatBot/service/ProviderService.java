package com.handson.ChatBot.service;


import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
public class ProviderService {


    public String searchProducts(String keyword) throws IOException {
        return parseMovieHtml(keyword);
    }

    private String parseMovieHtml(String keyword) throws IOException {
        String html = getMoviesHtml(keyword);  // this is your raw HTML
        StringBuilder res = new StringBuilder(); // use StringBuilder for building the result

        Matcher matcher = MOVIE_PATTERN.matcher(html);
        while (matcher.find()) {
            String title = matcher.group(1).trim();
            String year = matcher.group(2).trim();
            String actors = matcher.group(3).trim();

            res.append("🎬 ").append(title).append(" (").append(year).append(")").append("<br>")
                    .append("   ⭐ Actors: ").append(actors).append("<br>\n");
        }

        return res.toString().replaceAll("(?i)<br\\s*/?>", "\n");

    }


    // Regex to capture title, year/type, and actors from IMDb search results
    public static final Pattern MOVIE_PATTERN = Pattern.compile(
            "<a[^>]*class=\"ipc-metadata-list-summary-item__t\"[^>]*>([^<]+)</a>.*?" +           // Title
                    "<ul[^>]*ipc-metadata-list-summary-item__tl[^>]*>.*?<li[^>]*><span[^>]*>([^<]+)</span>.*?</ul>.*?" +  // Year / Type
                    "<ul[^>]*ipc-metadata-list-summary-item__stl[^>]*>.*?<li[^>]*><span[^>]*>([^<]+)</span>.*?</ul>",     // Actors
            Pattern.DOTALL
    );

    private String getMoviesHtml(String keyword) throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://www.imdb.com/find/?q=" + keyword)
                .method("GET", null)
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
                        "AppleWebKit/537.36 (KHTML, like Gecko) " +
                        "Chrome/140.0.0.0 Safari/537.36")
                .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8")
                .addHeader("Accept-Language", "en-US,en;q=0.5")
                .addHeader("Connection", "keep-alive")
                .addHeader("Upgrade-Insecure-Requests", "1")
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code " + response);
        }

        return response.body().string();
    }








}