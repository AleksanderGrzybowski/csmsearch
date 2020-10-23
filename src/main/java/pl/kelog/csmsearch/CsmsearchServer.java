package pl.kelog.csmsearch;

import spark.Request;
import spark.Response;
import spark.Spark;

import java.util.List;

import static java.util.stream.Collectors.joining;

public class CsmsearchServer {
    
    private static final int HTTP_STATUS_OK = 200;
    private static final int HTTP_STATUS_TEMPORARY_REDIRECT = 307;
    
    private final SongDatabase songDatabase;
    
    public CsmsearchServer(String databaseFilename) {
        this.songDatabase = new SongDatabase(databaseFilename);
    }
    
    public void start(int port) {
        Spark.port(port);
        
        Spark.get("/:part", this::handleRequest);
        
        Spark.before((request, response) -> System.out.println("Before request: " + request.ip() + " " + request.url()));
        Spark.after((request, response) -> System.out.println("After request: " + request.ip() + " " + request.url()));
    }
    
    private String handleRequest(Request request, Response response) {
        String part = request.params("part");
        
        System.out.println("Searching for <" + part + ">");
        List<Song> matches = songDatabase.findSongByPartOfTitle(part);
        System.out.println("Found: " + matches);
        
        if (matches.size() == 0) {
            response.status(HTTP_STATUS_OK);
            return "Nothing found.";
        } else if (matches.size() == 1) {
            response.status(HTTP_STATUS_TEMPORARY_REDIRECT);
            response.header("Location", matches.get(0).url);
            return "You are being redirected...";
        } else {
            response.status(HTTP_STATUS_OK);
            return renderMultipleLinks(matches);
        }
    }
    
    private static String renderMultipleLinks(List<Song> matches) {
        return matches.stream()
                .map(song -> "<a href=\"" + song.url + "\">" + song.title + "</a>")
                .collect(joining("<br/>"));
    }
}
