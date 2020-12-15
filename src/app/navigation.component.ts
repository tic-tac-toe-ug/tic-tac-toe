import {Component} from '@angular/core';
import {Routes} from "@angular/router";
import {HomeComponent} from "./home.component";
import {PageNotFoundComponent} from "./page-not-found.component";

@Component({
  selector: 'app-navigation',
  templateUrl: './navigation.component.html'
})
export class NavigationComponent { }

export const NavigationRoutes: Routes = [
  {path: '', component: HomeComponent},
  {path: '**', component: PageNotFoundComponent}
]
