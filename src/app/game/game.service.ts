import {Component, Injectable, OnInit} from '@angular/core';
import {HttpClient, HttpParams, HttpHeaders} from "@angular/common/http";
import {AlertService} from "../alert-component/alert.service";
import {Game} from "./game";

@Injectable()
export class GameService {

  private readonly gamesUrl;

  constructor(private http: HttpClient) {
    this.gamesUrl = 'http://localhost:8080/games';
  }

  public create(userName: string) {
    const params = new HttpParams()
      .set('user', userName)
      .set('privateGame', '1')  // TODO: this is temporary, need to add possibility to choose game type in frontend
    return this.http.put<Game>(this.gamesUrl+"/create", params);
  }

  public getJoinableGames() {
    const params = new HttpParams()
      .set('status', 'created')
    return this.http.get<Game[]>(this.gamesUrl, {params});
  }
  public getGame(id: number) {
    return this.http.get<Game>(this.gamesUrl+'/'+id);
  }
  public joinGame(id: number, userName: string) {
    const params = new HttpParams()
      .set('userId', userName)
    return this.http.post<String>(this.gamesUrl+'/'+id+'/join', params);
  }
  public play(id: number, userName: string, move: number) {
    var params = new HttpParams()
      .set('userId', userName)
      .set('move', move.toString());
    return this.http.post(this.gamesUrl+'/'+id+'/play', params, {responseType: 'text'});
  }

}


