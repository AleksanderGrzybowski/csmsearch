package pl.kelog.csmsearch;

public class Main {
    
    private static final String SONGS_DB_FILENAME = "songs-db.txt";
    private static final int PORT = 8080;
    
    public static void main(String[] args) {
        new CsmsearchServer(SONGS_DB_FILENAME).start(PORT);
    }
}
