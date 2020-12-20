import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppComponent} from './app.component';
import {HttpClientModule} from '@angular/common/http';
import {RouterModule} from '@angular/router';
import {PageNotFoundComponent} from './page-not-found.component';
import {HomeComponent} from './home.component';
import {NavigationComponent, NavigationRoutes} from './navigation.component';
import {TicTacToeComponent} from './tic-tac-toe/tic-tac-toe.component'
import {SquareComponent} from "./tic-tac-toe/square.component";
import {ReactiveFormsModule} from '@angular/forms';
import {RegisterFormComponent} from './register-form/register-form.component';
import { LoginComponent } from './login/login.component';
import {AlertModule} from "./alert-component/alert.module";


@NgModule({
  declarations: [
    AppComponent,
    NavigationComponent,
    PageNotFoundComponent,
    HomeComponent,
    TicTacToeComponent,
    SquareComponent,
    RegisterFormComponent,
    LoginComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    ReactiveFormsModule,
    RouterModule.forRoot(NavigationRoutes),
    AlertModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
