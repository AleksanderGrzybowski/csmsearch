This is a web service for searching the [CSM/online](https://csmonline.edu.pl) platform - very popular worship tutorials database.

Their website has a pretty bad search functionality. So I indexed all links and made my own search.

You can try it out via usual `npm start`.

Updating the index of all the links (you might have to update `LAST_PAGE_NUMBER` in scraper source): `EMAIL="heregoes@email.com" PASSWORD="heregoespassword" ./gradlew run`

