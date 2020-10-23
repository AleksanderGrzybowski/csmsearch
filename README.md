This is a web service for searching the [CSM/online](https://csmonline.edu.pl) platform - very popular worship tutorials database.

Their website has a pretty bad search functionality. So I indexed all links and made my own search. All you have to do is to hit [localhost:8080/<KEYWORD>](http
://localhost:8080/KEYWORD). You will be either redirected to the tutorial immediately (in case of 1 match on the song name), or to a page with list of search
 results.

You can try it out via usual right-click & run in IntelliJ, or via Docker:

```
docker build -t csmsearch .
docker run --rm -p 8080:8080 -ti csmsearch
```
