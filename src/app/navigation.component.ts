import {Component, Injectable} from '@angular/core';
import {Router, Routes} from "@angular/router";
import {HomeComponent} from "./home.component";
import {PageNotFoundComponent} from "./page-not-found.component";
import {TicTacToeComponent} from "./tic-tac-toe/tic-tac-toe.component";
import {JoinGameComponent} from "./tic-tac-toe/join-game.component";
import {RegisterFormComponent} from "./register-form/register-form.component";
import {LoginComponent} from "./login/login.component";
import {RankingComponent} from "./ranking/ranking.component";
import {SecurityService} from "./login/security.service";
import {AlertService} from "./alert-component/alert.service";
import {ShowUserComponent} from "./user/show-user.component";

@Component({
  selector: 'app-navigation',
  templateUrl: './navigation.component.html'
})
@Injectable()
export class NavigationComponent {

  securityService: SecurityService

  constructor(securityService: SecurityService,
              private alertService: AlertService,
              private router: Router) {
    this.securityService = securityService;
  }

  logout() {
    this.securityService.logout()
      .subscribe(
        (_: any) => {
          this.alertService.success("Wylogowano!", {keepAfterRouteChange: true});
          this.router.navigateByUrl("/")
        },
        (error: any) => {
          this.alertService.error(error.errors)
        }
      )
  }

}

export const NavigationRoutes: Routes = [
  {path: '', component: HomeComponent},
  {path: 'play-game/:id', component: TicTacToeComponent},
  {path: 'join-game', component: JoinGameComponent},
  {path: 'register-form', component: RegisterFormComponent},
  {path: 'ranking', component: RankingComponent},
  {path: 'login-form', component: LoginComponent},
  {path: 'user/:username', component: ShowUserComponent},
  {path: '**', component: PageNotFoundComponent}
]
