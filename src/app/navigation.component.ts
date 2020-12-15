import {Component} from '@angular/core';
import {Routes} from "@angular/router";
import {HomeComponent} from "./home.component";
import {PageNotFoundComponent} from "./page-not-found.component";
import {TicTacToeComponent} from "./tic-tac-toe/tic-tac-toe.component";

@Component({
  selector: 'app-navigation',
  templateUrl: './navigation.component.html'
})
export class NavigationComponent { }

export const NavigationRoutes: Routes = [
  {path: '', component: HomeComponent},
  {path: 'play-game', component: TicTacToeComponent},
  {path: '**', component: PageNotFoundComponent}
]
