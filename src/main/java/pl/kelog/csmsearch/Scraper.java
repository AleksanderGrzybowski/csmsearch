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

 * Alternatively, ignore IntelliJ and run the following from Unix shell:
 * $ EMAIL="heregoes@email.com" PASSWORD="heregoespassword" ./gradlew run
 */
public class Scraper {

    /** The main CSM Online website address */
    private static final String BASE_URL = "https://csmonline.edu.pl";

    private static final String EMAIL = System.getenv("EMAIL");
    private static final String PASSWORD = System.getenv("PASSWORD");

    /** This needs updating every time a new page of results is added to CSM Online */
    private static final int LAST_PAGE_NUMBER = 12;

    /** A path, where frontend code expects JSON file with songs to exist */
    private static final String SONGS_DB_FILENAME = "csmsearch/src/songs.js";

    private static final Gson GSON = new Gson();
    static Type GSON_SONG_LIST_TYPE = new TypeToken<List<Song>>() {
    }.getType();

    public static void main(String[] args) throws IOException {
        System.out.println("Requesting login page to get CSRF token (from PHP framework) before anything else...");
        Response initialResponse = Jsoup.connect(BASE_URL + "/login").execute();
        String token = initialResponse.parse().select("input").val();
        System.out.println("Got _token: " + token);

        System.out.println("Logging in as " + EMAIL + "...");
        Response loginResponse = Jsoup.connect(BASE_URL + "/login")
                .data(createLoginPostRequestData(token))
                .cookies(initialResponse.cookies())
                .method(Connection.Method.POST)
                .execute();
        
        List<Song> songs = new ArrayList<>();
        
        for (int pageNumber = 1; pageNumber <= LAST_PAGE_NUMBER; ++pageNumber) {
            songs.addAll(getSongsForPage(pageNumber, loginResponse.cookies()));
            System.out.println("Current count of songs: " + songs.size());
        }

        writeSongsToFile(songs);
        System.out.println("Successfully wrote " + songs.size() + " songs in total.");
    }

    private static Map<String, String> createLoginPostRequestData(String token) {
        Map<String, String> postData = new HashMap<>();
        postData.put("email", EMAIL);
        postData.put("password", PASSWORD);
        postData.put("_token", token);
        return postData;
    }

    private static List<Song> getSongsForPage(int pageNumber, Map<String, String> cookies) throws IOException {
        System.out.println("Listing page " + pageNumber + "...");
        Document songsListPage = Jsoup.connect(BASE_URL + "/sheet-music?page=" + pageNumber)
                .cookies(cookies)
                .execute().parse();

        String jsonSongsData = songsListPage.select("v-data-table").attr(":items");
        System.out.println("Got JSON object with a list of songs, raw size = " + jsonSongsData.length() + " bytes.");

        return GSON.fromJson(jsonSongsData, GSON_SONG_LIST_TYPE);
    }

    private static void writeSongsToFile(List<Song> songs) throws IOException {
        String serializedJson = new Gson().toJson(songs, GSON_SONG_LIST_TYPE);
        String songsFileContent = "const data = " + serializedJson + "\nexport default data;";
        FileUtils.writeStringToFile(new File(SONGS_DB_FILENAME), songsFileContent, Charset.defaultCharset());
    }
}

