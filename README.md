# CSMsearch

## What is this?

This is a web service for searching the [CSM/online](https://csmonline.edu.pl) platform - very popular Polish
worship tutorials database.

## What it is for?

CSM/online website has a very lacking search functionality, no autocomplete despite the fact, that there are only few hundred songs. So I created a scraper, that indexes all the links.  Then, a simple webapp with better client-side search can be used to find them faster.

## How it works?

* Java application uses *jsoup* library to log in to the website, impersonating my user. It scrapes HTML finding all songs, then creates JSON file with URLs to them.
* Simple React app reads this JSON file and presents it as a list, with autocomplete search.

## How to run it?

You can try it out immediately via usual `npm start`. Updating the index of all the links can be done as follows:

* update `LAST_PAGE_NUMBER` variable in scraper source with the number of last page on the website (automatic detection is not implemented)
* run `EMAIL="heregoes@email.com" PASSWORD="heregoespassword" ./gradlew run` with Java 8 present in the shell


![Screenshot 1](screenshot.png)
