package pl.kelog.csmsearch;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import static java.util.stream.Collectors.toList;

class SongDatabase {
    
    public static final String SEPARATOR = " \\| ";
    static List<Song> songs = new ArrayList<>();
    
    public SongDatabase(String databaseFilename) {
        System.out.println("Reading songs from " + databaseFilename + "...");
        
        byte[] content;
        try {
            content = Files.readAllBytes(new File(databaseFilename).toPath());
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        
        String decoded = new String(Base64.getMimeDecoder().decode(content));
        
        Arrays.stream(decoded.split("\n")).forEach(line -> {
            String[] split = line.split(SEPARATOR);
            Song song = new Song(split[1], split[0]);
            songs.add(song);
            System.out.println("Added song " + song);
        });
        
        System.out.println("Reading finished, count = " + songs.size());
    }
    
    public List<Song> findByPartOfTitle(String part) {
        return songs.stream()
                .filter(song -> Utils.normalize(song.title).contains(part))
                .collect(toList());
    }
}
