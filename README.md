let cards = $$('a.v-card');
cards.forEach(card => {
   let href = card.getAttribute('href');
   let title = card.querySelector('div.v-card__text').querySelector('h3').textContent;
   console.log(`${href} | ${title}`);
})

