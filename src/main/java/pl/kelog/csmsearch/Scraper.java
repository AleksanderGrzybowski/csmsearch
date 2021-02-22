package pl.kelog.csmsearch;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.FileUtils;
import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.*;


/**
 * This is a simple Jsoup scraper. Just fill in credentials and run it from IntelliJ,
 * then commit the result.
 */
public class Scraper {
    
    private static final String BASE_URL = "https://csmonline.edu.pl";
    private static final String EMAIL = "here@is.email";
    private static final String PASSWORD = "here_is_password";
    private static final int LAST_PAGE_NUMBER = 8;
    
    private static final Gson GSON = new Gson();
    static Type GSON_SONG_LIST_TYPE = new TypeToken<List<Song>>() {
    }.getType();
    
    public static void main(String[] args) throws IOException {
        
        
        // this initial query is required to get magic _token for some reason (I guess it is the PHP framework they use)
        System.out.println("Visiting initial page...");
        Response initialResponse = Jsoup.connect(BASE_URL + "/login").execute();
        String token = initialResponse.parse().select("input").val();
        System.out.println("Got _token: " + token);
        
        System.out.println("Logging in as " + EMAIL + "...");
        Map<String, String> postData = new HashMap<>();
        postData.put("email", EMAIL);
        postData.put("password", PASSWORD);
        postData.put("_token", token);
        Response loginResponse = Jsoup.connect(BASE_URL + "/login")
                .data(postData)
                .cookies(initialResponse.cookies())
                .method(Connection.Method.POST)
                .execute();
        
        List<Song> songs = new ArrayList<>();
        
        for (int pageNumber = 1; pageNumber <= LAST_PAGE_NUMBER; ++pageNumber) {
            System.out.println("Listing page " + pageNumber + "...");
            Document songsListPage = Jsoup.connect(BASE_URL + "/sheet-music?page=" + pageNumber)
                    .cookies(loginResponse.cookies())
                    .execute().parse();
            
            String jsonData = songsListPage.select("v-data-table").attr(":items");
            System.out.println("Got JSON object with a list of songs, raw size = " + jsonData.length() + " bytes.");
            
            List<Song> parsedSongsFromPage = GSON.fromJson(jsonData, GSON_SONG_LIST_TYPE);
            System.out.println("Adding " + parsedSongsFromPage.size() + " songs to the list.");
            
            songs.addAll(parsedSongsFromPage);
        }
        
        String serializedJson = new Gson().toJson(songs, GSON_SONG_LIST_TYPE);
        
        String encodedJson = new String(Base64.getMimeEncoder().encode(serializedJson.getBytes()));
        FileUtils.writeStringToFile(new File(Config.SONGS_DB_FILENAME), encodedJson, Charset.defaultCharset());
    }
}

