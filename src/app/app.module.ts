import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppComponent} from './app.component';
import {HttpClientModule} from '@angular/common/http';
import {RouterModule} from '@angular/router';
import {PageNotFoundComponent} from './page-not-found.component';
import {HomeComponent} from './home.component';
import {NavigationComponent, NavigationRoutes} from './navigation.component';
import {TicTacToeComponent} from './tic-tac-toe/tic-tac-toe.component'
import {JoinGameComponent} from './tic-tac-toe/join-game.component'
import {SquareComponent} from "./tic-tac-toe/square.component";
import {ReactiveFormsModule} from '@angular/forms';
import {RegisterFormComponent} from './register-form/register-form.component';
import {LoginComponent} from './login/login.component';
import {AlertModule} from "./alert-component/alert.module";
import {RankingComponent} from './ranking/ranking.component';
import {SecurityService} from "./login/security.service";
import {CookieService} from 'ng2-cookies';
import {ShowUserComponent} from "./user/show-user.component";
import {ListGamesComponent} from "./game/list-games.component";


@NgModule({
  declarations: [
    AppComponent,
    NavigationComponent,
    PageNotFoundComponent,
    HomeComponent,
    TicTacToeComponent,
    JoinGameComponent,
    SquareComponent,
    RegisterFormComponent,
    LoginComponent,
    RankingComponent,
    ShowUserComponent,
    ListGamesComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    ReactiveFormsModule,
    RouterModule.forRoot(NavigationRoutes),
    AlertModule
  ],
  providers: [SecurityService, CookieService],
  bootstrap: [AppComponent]
})
export class AppModule {
}
