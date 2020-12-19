import {Component} from '@angular/core';
import {Routes} from "@angular/router";
import {HomeComponent} from "./home.component";
import {PageNotFoundComponent} from "./page-not-found.component";
import {TicTacToeComponent} from "./tic-tac-toe/tic-tac-toe.component";
import {RegisterFormComponent} from "./register-form/register-form.component";
import {LoginComponent} from "./login/login.component";

@Component({
  selector: 'app-navigation',
  templateUrl: './navigation.component.html'
})
export class NavigationComponent {
}

export const NavigationRoutes: Routes = [
  {path: '', component: HomeComponent},
  {path: 'play-game', component: TicTacToeComponent},
  {path: 'register-form', component: RegisterFormComponent},
  {path: 'login-form', component: LoginComponent},
  {path: '**', component: PageNotFoundComponent}
]
