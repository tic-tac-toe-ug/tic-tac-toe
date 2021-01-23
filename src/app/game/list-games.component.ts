import {Component, Injectable, OnInit} from '@angular/core';
import {AlertService} from "../alert-component/alert.service";
import {GameService} from "./game.service";
import {Game} from "./game";

@Component({
  selector: 'app-list-games',
  templateUrl: './list-games.component.html',
  providers: [GameService]
})
@Injectable()
export class ListGamesComponent implements OnInit {

  public entries: Game[]

  constructor(private gameService: GameService,
              private alertService: AlertService) {
  }

  ngOnInit(): void {
    this.gameService.listGames().subscribe(
      (games: Game[]) => {
        this.entries = games
      },
      (errorResponse: any) => {
        errorResponse.error.errors
          .map((x: any) => x.defaultMessage)
          .forEach((message: string) => this.alertService.error(message))
      })
  }

  deleteGame(id: number) {
    this.gameService.deleteGame(id).subscribe(
      (games: Game[]) => {
        this.entries = games
      },
      (errorResponse: any) => {
        errorResponse.error.errors
          .map((x: any) => x.defaultMessage)
          .forEach((message: string) => this.alertService.error(message))
      })
  }
}
