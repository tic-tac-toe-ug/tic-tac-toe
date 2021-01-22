import {Component, OnInit} from '@angular/core';
import {GameService} from "../game/game.service";
import {Game} from "../game/game";
import {AlertService} from "../alert-component/alert.service";
import {Router} from "@angular/router";
import {FormBuilder, FormGroup, Validators} from '@angular/forms';

@Component({
  selector: 'app-join-game',
  templateUrl: './join-game.component.html',
  providers: [GameService]
})
export class JoinGameComponent implements OnInit {
  games: Game[]
  loading = false;
  loading_join = false;
  form: FormGroup;
  submitted = false;

  constructor(
    private formBuilder: FormBuilder,
    private alertService: AlertService,
    private gameService: GameService,
    private router: Router
    )
    {}


  get f() {
    return this.form.controls;
  }

  ngOnInit(): void
  {
    this.form = this.formBuilder.group({
      login: ['', [Validators.required]]
    });

    // TODO: get only public games
    this.gameService.getJoinableGames().subscribe(
      (games: Game[]) => {
        this.games = games
      },
      (errorResponse: any) => {
        errorResponse.error.errors
          .map((x: any) => x.defaultMessage)
          .forEach((message: string) => this.alertService.error(message))
      }
    )
  }

  onNewGame() {
    this.submitted = true;

    if (this.form.invalid) {
      return;
    }

    this.loading = true;
    this.gameService.create(this.form.getRawValue().login).subscribe(
      (game: Game) => {
        this.alertService.success("Stworzono nową grę", {keepAfterRouteChange: true});
        this.router.navigate(['/play-game', game.id])
      },
      (errorResponse: any) => {
        errorResponse.error.errors
          .map((x: any) => x.defaultMessage)
          .forEach((message: string) => this.alertService.error(message))
        this.loading = false;
      }
    )
  }
  onJoin(id: number) {
    var name = 'guest'
    this.loading_join = true;
    this.gameService.joinGame(id, name).subscribe(
      (str: String) => {
        this.alertService.success("Dołączyłeś jako " + name, {keepAfterRouteChange: true});
        this.router.navigate(['/play-game', id])
      },
      (errorResponse: any) => {

        errorResponse.error.errors
          .map((x: any) => x.defaultMessage)
          .forEach((message: string) => this.alertService.error(message))
        this.loading_join = false;
      }
    )
  }
}
