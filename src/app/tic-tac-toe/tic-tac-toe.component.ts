import {Component, OnInit} from '@angular/core';
import {GameService} from "../game/game.service";
import {Game} from "../game/game";
import {AlertService} from "../alert-component/alert.service";
import {Router} from "@angular/router";
import { map } from 'rxjs/operators';

import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-tic-tac-toe',
  templateUrl: './tic-tac-toe.component.html',
  providers: [GameService]
})
export class TicTacToeComponent implements OnInit {
  game: Game
  squares: any[] = [];
  playerOneTurn: boolean = false;
  winner?: string;
  id: number
  private sub: any;

  constructor(
    private alertService: AlertService,
    private gameService: GameService,
    private router: Router,
    private route: ActivatedRoute
)
{
  }

  ngOnInit(): void {
  this.sub =this.route.params.subscribe(params => {
         this.id = +params['id'];
      });
    this.newGame();
  }

  ngOnDestroy() {
    this.sub.unsubscribe();
  }

  private newGame() {
    this.gameService.getGame(this.id).subscribe(
      (game: Game) => {
        this.squares = Array(9).fill('')
        this.game = game
      },
      (errorResponse: any) => {
        errorResponse.error.errors
          .map((x: any) => x.defaultMessage)
          .forEach((message: string) => this.alertService.error(message))
      })
  }

  get player() {
    return this.playerOneTurn ? 'O' : 'X'
  }

  makeMove(idx: number) {
    if (this.squares[idx] === '') {
      this.gameService.play(this.game.id, this.game.firstToMove, idx+1)
      .subscribe(
            data => {
              this.alertService.success(data.toString())
              this.squares.splice(idx, 1, this.player);
              this.playerOneTurn = !this.playerOneTurn
              if (this.game.firstToMove == this.game.user1)
                this.game.firstToMove = this.game.user2
              else
                this.game.firstToMove = this.game.user1
            },
            error => {
              this.alertService.error(error.error)
              error.errors
                .map((x: any) => x.defaultMessage)
                .forEach((message: string) => this.alertService.error(message))
            }
      )
    }
  }


}
