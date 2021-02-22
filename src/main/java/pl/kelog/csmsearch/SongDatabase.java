package pl.kelog.csmsearch;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.Base64;
import java.util.List;

import static java.util.stream.Collectors.toList;

class SongDatabase {
    
    private static final Gson GSON = new Gson();
    private static final Type GSON_SONGS_LIST_TYPE = new TypeToken<List<Song>>() {
    }.getType();
    private final List<Song> songs;
    
    public SongDatabase(String databaseFilename) {
        System.out.println("Reading songs from " + databaseFilename + "...");
        
        songs = readSongsDatabase(databaseFilename);
        
        System.out.println("Reading finished, count = " + songs.size() + " songs.");
    }
    
    public List<Song> findSongByPartOfTitle(String part) {
        return songs.stream()
                .filter(song -> Utils.normalize(song.title).contains(part))
                .collect(toList());
    }
    
    private List<Song> readSongsDatabase(String databaseFilename) {
        byte[] content;
        
        try {
            content = Files.readAllBytes(new File(databaseFilename).toPath());
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        
        System.out.println("Loaded " + content.length + " bytes of base64-d data.");
        
        String decodedJson = new String(Base64.getMimeDecoder().decode(content));
        System.out.println("Decoded " + decodedJson.length() + " bytes of JSON data.");
    
        System.out.println("Parsing JSON...");
        return GSON.fromJson(decodedJson, GSON_SONGS_LIST_TYPE);
    }
}
