package pl.kelog.csmsearch;

public class Main {
    
    public static void main(String[] args) {
        new CsmsearchServer(Config.SONGS_DB_FILENAME).start(Config.PORT);
    }
}
