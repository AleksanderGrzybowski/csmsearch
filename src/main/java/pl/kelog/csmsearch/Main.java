package pl.kelog.csmsearch;

import spark.Request;
import spark.Response;
import spark.Spark;

import java.util.List;
import java.util.stream.Collectors;

public class Main {
    
    static SongDatabase songDatabase = new SongDatabase("db.txt");
    
    public static void main(String[] args) {
        Spark.port(8080);
        Spark.get("/:part", (Main::handleRequest));
        
        Spark.before((request, response) -> System.out.println("Request: " + request.ip() + " " + request.url()));
    }
    
    private static String handleRequest(Request request, Response response) {
        List<Song> matches = songDatabase.findByPartOfTitle(request.params("part"));
        
        if (matches.size() == 0) {
            response.status(200);
            return "Nothing found.";
        } else if (matches.size() == 1) {
            response.status(307);
            response.header("Location", matches.get(0).url);
            return "You are being redirected...";
        } else {
            response.status(200);
            return renderMultipleLinks(matches);
        }
    }
    
    private static String renderMultipleLinks(List<Song> matches) {
        return matches.stream()
                .map(song -> "<a href=\"" + song.url + "\">" + song.title + "</a>")
                .collect(Collectors.joining("<br/>"));
    }
}
