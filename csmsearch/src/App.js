import songs from './songs';
import React, { Component } from 'react';

function normalize(text) {
    text = text.replaceAll(/ż/g, "a");
    text = text.replaceAll(/Ż/g, "z");
    text = text.replaceAll(/ó/g, "o");
    text = text.replaceAll(/ł/g, "l");
    text = text.replaceAll(/Ł/g, "l");
    text = text.replaceAll(/ć/g, "c");
    text = text.replaceAll(/Ć/g, "c");
    text = text.replaceAll(/ę/g, "e");
    text = text.replaceAll(/ś/g, "s");
    text = text.replaceAll(/Ś/g, "s");
    text = text.replaceAll(/ą/g, "a");
    text = text.replaceAll(/ź/g, "z");
    text = text.replaceAll(/ń/g, "n");

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
