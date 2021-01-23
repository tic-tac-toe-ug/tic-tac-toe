import {Component, OnInit} from '@angular/core';
import {GameService} from "../game/game.service";
import {Game} from "../game/game";
import {AlertService} from "../alert-component/alert.service";
import {Router} from "@angular/router";
import {SecurityService} from "../login/security.service";
import {FormBuilder, FormGroup, Validators} from '@angular/forms';


@Component({
  selector: 'app-join-game',
  templateUrl: './join-game.component.html',
  providers: [GameService, SecurityService]
})
export class JoinGameComponent implements OnInit {
  games: Game[]
  myGames: Game[]
  loading_private = false;
  loading_public = false;
  loading_join = false;
  hide_form = true;
  username = "";
  form: FormGroup;
  submitted = false;

  constructor(
    private formBuilder: FormBuilder,
    private alertService: AlertService,
    private gameService: GameService,
    private router: Router,
    private securityService: SecurityService
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
    this.securityService.loadCookie();
    if(this.securityService.isLogged() && this.securityService.user != undefined){
      this.hide_form = false;
      this.username = this.securityService.user.username;
    }

    this.gameService.getJoinableGames().subscribe(
      (games: Game[]) => {
      this.games = games
      this.games = this.games.filter(item => item.user1 !== this.username);
      },
      (errorResponse: any) => {
        errorResponse.error.errors
          .map((x: any) => x.defaultMessage)
          .forEach((message: string) => this.alertService.error(message))
      }
    )

    this.gameService.getMyGames().subscribe(
      (games: Game[]) => {
      this.myGames = games
      this.myGames = this.myGames.filter(item => item.user1 === this.username || item.user2 === this.username);
      },
      (errorResponse: any) => {
        errorResponse.error.errors
          .map((x: any) => x.defaultMessage)
          .forEach((message: string) => this.alertService.error(message))
      }
    )
  }
  onNewPublicGame(){
    this.loading_public = true;
    this.onNewGame(false, "")
  }
  onNewPrivateGame(){
    this.submitted = true;
    if (this.form.invalid) {
      return;
    }
    this.loading_private = true;
    this.onNewGame(true, this.form.getRawValue().login)
  }

  private onNewGame(is_private: boolean, user2: string) {
    if(this.securityService.user != undefined){
      this.gameService.create(this.username, is_private, user2).subscribe(
        data => {
          this.alertService.success("Stworzono nową grę", {keepAfterRouteChange: true});
          this.router.navigate(['/play-game', data])
        },
        error => {
          this.alertService.error("Gracz nie istnieje")
          this.loading_private = false;
          this.loading_public = false;
        }
      )
    }
  }

  onJoin(id: number) {
    this.loading_join = true;
    this.gameService.joinGame(id, this.username).subscribe(
      (str: String) => {
        this.alertService.success("Dołączyłeś jako " + this.username, {keepAfterRouteChange: true});
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

  openMyGame(id: number) {
    this.router.navigate(['/play-game', id])
  }
}
