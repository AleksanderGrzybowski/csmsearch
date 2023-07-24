import songs from './songs';
import React, { Component } from 'react';

function normalize(text) {
    text = text.replace(/ż/, "a");
    text = text.replace(/Ż/, "z");
    text = text.replace(/ó/, "o");
    text = text.replace(/ł/, "l");
    text = text.replace(/Ł/, "l");
    text = text.replace(/ć/, "c");
    text = text.replace(/Ć/, "c");
    text = text.replace(/ę/, "e");
    text = text.replace(/ś/, "s");
    text = text.replace(/Ś/, "s");
    text = text.replace(/ą/, "a");
    text = text.replace(/ź/, "z");
    text = text.replace(/ń/, "n");
    
    return text.toLowerCase();
}

class App extends Component {

  constructor(props) {
    super(props);

    this.state = {
      text: '',
      helpVisible: false
    };
  }

  filterSongs() {
    return songs.filter(s => normalize(s.title).includes(normalize(this.state.text)));
  }

  handleTextChange = event => {
     this.setState({text: event.target.value})
  }

  toggleHelpVisible = () => {
     this.setState({helpVisible: !this.state.helpVisible})
  }

  handleKeyDown = event => {
     if (event.key === "Escape") {
       this.setState({text: ''});
     }
     if (event.key === "Enter") {
       const filteredSongs = this.filterSongs();
       this.setState({text: ''});
       window.open(filteredSongs[0].url);
     }
  }

  render() {
    const songs = this.filterSongs().map(song =>
      <li key={song.url}>
        <a href={song.url}>{song.title}</a>
      </li>
    );

    const titleText = <p>
      <b>Lepsza wyszukiwarka CSM Online</b>&nbsp;
      <u style={{cursor: 'pointer'}} onClick={this.toggleHelpVisible}>jak używać?</u>
    </p>;

    const helpText = <div>
      <p>Zacznij pisać (wystarczy fragment tytułu), klawisz ENTER otwiera pierwszy znaleziony wynik na liście, ESC czyści.</p>
      <p>Niektóre utwory nie mają wideo tutoriali - w takim przypadku pokaże się błąd 404.</p>
    </div>;

    return (
      <div>
      {titleText}
      {this.state.helpVisible && helpText}
      <input autoFocus type="text" value={this.state.text} onChange={this.handleTextChange} onKeyDown={this.handleKeyDown} />
      <ul>
          {songs}
      </ul>
      </div>
    );
  }
}

export default App;
