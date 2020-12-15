import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppComponent} from './app.component';
import {HttpClientModule} from '@angular/common/http';
import {RouterModule} from '@angular/router';
import {PageNotFoundComponent} from './page-not-found.component';
import {HomeComponent} from './home.component';
import {NavigationRoutes, NavigationComponent} from './navigation.component'


@NgModule({
  declarations: [
    AppComponent,
    NavigationComponent,
    PageNotFoundComponent,
    HomeComponent
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
