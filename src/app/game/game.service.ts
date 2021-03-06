import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {Game} from "./game";
import {SecurityService} from "../login/security.service";
import {Observable} from "rxjs";

@Injectable()
export class GameService {

  private readonly gamesUrl;

  constructor(private http: HttpClient, private securityService: SecurityService) {
    this.gamesUrl = 'http://localhost:8080/games';
  }

  public create(userName: string, is_private: boolean, user2: string) {
    const params = new HttpParams()
      .set('user', userName)
      .set('privateGame', is_private.toString())
      .set('user2', user2)
    return this.http.put(this.gamesUrl + "/create", params, {responseType: 'text'});
  }

  public getJoinableGames() {
    const params = new HttpParams()
      .set('status', 'created')
    return this.http.get<Game[]>(this.gamesUrl, {params});
  }

  public getMyGames() {
    return this.http.get<Game[]>(this.gamesUrl);
  }

  public getGame(id: number) {
    return this.http.get<Game>(this.gamesUrl + '/' + id);
  }

  public joinGame(id: number, userName: string) {
    const params = new HttpParams()
      .set('userId', userName)
    return this.http.post<String>(this.gamesUrl + '/' + id + '/join', params);
  }

  public play(id: number, userName: string, move: number) {
    var params = new HttpParams()
      .set('userId', userName)
      .set('move', move.toString());
    return this.http.post(this.gamesUrl + '/' + id + '/play', params, {responseType: 'text'});
  }

  public listGames(): Observable<Game[]> {
    if (this.securityService.isAdmin()) {
      return this.http.get<Game[]>(this.gamesUrl)
    } else {
      return new Observable<Game[]>()
    }
  }

  deleteGame(id: number): Observable<Game[]> {
    if (this.securityService.isAdmin()) {
      return this.http.delete<Game[]>(this.gamesUrl + "/" + id);
    } else {
      return new Observable<Game[]>()
    }
  }
}


