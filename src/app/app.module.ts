import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppComponent} from './app.component';
import {HttpClientModule} from '@angular/common/http';
import {RouterModule} from '@angular/router';
import {PageNotFoundComponent} from './page-not-found.component';
import {HomeComponent} from './home.component';
import {NavigationRoutes, NavigationComponent} from './navigation.component';
import { TicTacToeComponent } from './tic-tac-toe/tic-tac-toe.component'
import {SquareComponent} from "./tic-tac-toe/square.component";


@NgModule({
  declarations: [
    AppComponent,
    NavigationComponent,
    PageNotFoundComponent,
    HomeComponent,
    TicTacToeComponent,
    SquareComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    RouterModule.forRoot(NavigationRoutes)
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
