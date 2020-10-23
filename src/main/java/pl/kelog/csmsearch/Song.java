package pl.kelog.csmsearch;

class Song {
    public final String title;
    public final String url;
    
    Song(String title, String url) {
        this.title = title;
        this.url = url;
    }
    
    @Override
    public String toString() {
        return "Song{" +
                "title='" + title + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
