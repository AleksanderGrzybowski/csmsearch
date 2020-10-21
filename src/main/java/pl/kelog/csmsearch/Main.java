package pl.kelog.csmsearch;

import spark.Request;
import spark.Response;
import spark.Spark;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

class Song {
    public final String title;
    public final String url;
    
    Song(String title, String url) {
        this.title = title;
        this.url = url;
    }
}

class SongDatabase {
    
    static List<Song> songs = new ArrayList<>();
    
    public SongDatabase() {
        try {
            byte[] content = Files.readAllBytes(new File("db.txt").toPath());
            String decoded = new String(Base64.getMimeDecoder().decode(content));
            Arrays.stream(decoded.split("\n")).forEach(line -> {
                String[] split = line.split(" \\| ");
                System.out.println(split[0]);
                System.out.println(split[1]);
                songs.add(new Song(split[1], split[0]));
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public List<Song> findByPartOfTitle(String part) {
        return songs.stream().filter(song -> {
            String normalizedTitle = song.title.toLowerCase().replace("ą", "a").replace("ę", "e").replace("ó", "o").replace("ż", "z").replace("ź", "z").replace("ł", "l")
                    .toLowerCase();
            return normalizedTitle.contains(part);
        }).collect(toList());
    }
}

public class Main {
    
    static SongDatabase songDatabase = new SongDatabase();
    
    public static void main(String[] args) {
        Spark.port(8080);
        Spark.get("/:part", (Main::handle));
    }
    
    private static Object handle(Request request, Response response) {
        List<Song> matches = songDatabase.findByPartOfTitle(request.params("part"));
        
        if (matches.size() == 0) {
            response.status(200);
            return "Nothing found";
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
        return matches.stream().map(song -> "<a href=\"" + song.url + "\">" + song.title + "</a>")
                .collect(Collectors.joining("<br/>"));
    }
}
