import {Component, OnInit} from '@angular/core';
import {GameService} from "../game/game.service";
import {Game} from "../game/game";
import {AlertService} from "../alert-component/alert.service";
import {Router} from "@angular/router";
import { map } from 'rxjs/operators';
import {SecurityService} from "../login/security.service";
import {interval} from "rxjs/internal/observable/interval";
import {startWith, switchMap} from "rxjs/operators";

import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-tic-tac-toe',
  templateUrl: './tic-tac-toe.component.html',
  providers: [GameService, SecurityService]
})
export class TicTacToeComponent implements OnInit {
  game: Game;
  squares: any[] = [];
  id: number;
  private sub: any;
  username = '';
  player = '';
  private interv: any;
  players = new Map<string, string>();

  constructor(
    private alertService: AlertService,
    private gameService: GameService,
    private router: Router,
    private route: ActivatedRoute,
    private securityService: SecurityService
)
{
  }

  ngOnInit(): void {
  this.sub =this.route.params.subscribe(params => {
         this.id = +params['id'];
      });
    this.securityService.loadCookie();
    this.newGame();
    if(this.securityService.user != undefined){
      this.username = this.securityService.user.username;
    }
  }

  ngOnDestroy() {
    this.sub.unsubscribe();
  }

  private newGame() {
    this.interv = interval(200)
               .pipe(
                 startWith(0),
                 switchMap(() => this.gameService.getGame(this.id)))
               .subscribe(
                  (game: Game) => {
                    this.squares = Array(9).fill('');
                    for (let i=0;i<9;i++){
                      if(game.moves[i] == 1) this.squares[i] = 'O';
                      if(game.moves[i] == 2) this.squares[i] = 'X';
                    }
                    this.game = game;

                    this.players.set(this.game.user1, 'O');
                    this.players.set(this.game.user2, 'X');
                    if (this.game.firstToMove != undefined && this.players.get(this.game.firstToMove) != undefined)
                      this.player = this.players.get(this.game.firstToMove)!
                    if(this.game.gameStatus !== "CREATED" && this.game.gameStatus !== "IN_PROGRESS") {
                      this.interv.unsubscribe();

                      if(this.game.gameStatus === "TIE")
                        this.alertService.success("Remis")
                      else if((this.game.gameStatus === "USER1_WON" && this.game.user1 === this.username) ||
                          (this.game.gameStatus === "USER2_WON" && this.game.user2 === this.username))
                        this.alertService.success("Wygrałeś")
                      else
                        this.alertService.error("Przegrałeś")
                    }
                  },
                  (errorResponse: any) => {
                    errorResponse.error.errors
                      .map((x: any) => x.defaultMessage)
                      .forEach((message: string) => this.alertService.error(""))
                  }
               )
  }

  makeMove(idx: number) {
    if (this.squares[idx] === '') {
      this.gameService.play(this.game.id, this.username, idx+1)
      .subscribe(
            data => {
              this.squares.splice(idx, 1, this.players.get(this.username)!);
              if (this.game.firstToMove == this.game.user1)
                this.game.firstToMove = this.game.user2
              else
                this.game.firstToMove = this.game.user1
            },
            error => {
              error.errors
                .map((x: any) => x.defaultMessage)
                .forEach((message: string) => this.alertService.error(""))
            }
      )
    }
  }
}
