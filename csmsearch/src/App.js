import songs from './songs';
import React, { Component } from 'react';

function normalize(text) {
    text = text.replace(/ą/, "a");
    text = text.replace(/ę/, "e");
    text = text.replace(/ó/, "o");
    text = text.replace(/ż/, "z");
    text = text.replace(/ź/, "z");
    text = text.replace(/ł/, "l");
    text = text.replace(/ś/, "s");
    text = text.replace(/ń/, "n");
    text = text.replace(/ć/, "c");

    return text.toLowerCase();
}

class App extends Component {

  constructor(props) {
    super(props);

    this.state = {
      text: ''
    };
  }

  filterSongs() {
    return songs.filter(s => normalize(s.title).includes(normalize(this.state.text)));
  }

  handleTextChange = event => {
     this.setState({text: event.target.value})
  }

  handleKeyDown = event => {
     const filteredSongs = this.filterSongs();
     if (event.key === "Enter") {
         window.location.href = filteredSongs[0].url;
     }
  }

  render() {
    const songs = this.filterSongs().map(song =>
      <li key={song.url}>
        <a href={song.url}>{song.title}</a>
      </li>
    )

    return (
      <div>
      <input autoFocus type="text" value={this.state.text} onChange={this.handleTextChange} onKeyDown={this.handleKeyDown} />
      <ul>
          {songs}
      </ul>
      </div>
    );
  }
}

export default App;
