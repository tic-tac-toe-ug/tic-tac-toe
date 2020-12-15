import {Component, OnInit} from '@angular/core';

@Component({
  selector: 'app-tic-tac-toe',
  templateUrl: './tic-tac-toe.component.html'
})
export class TicTacToeComponent implements OnInit {
  squares: any[] = [];
  playerOneTurn: boolean = false;
  winner?: string;

  constructor() {
  }

  ngOnInit(): void {
    this.newGame();
  }

  private newGame() {
    this.squares = Array(9).fill('')
  }

  get player() {
    return this.playerOneTurn ? 'O' : 'X'
  }

  makeMove(idx: number) {
    if (this.squares[idx] === '') {
      this.squares.splice(idx, 1, this.player);
      this.playerOneTurn = !this.playerOneTurn
    }
  }


}
