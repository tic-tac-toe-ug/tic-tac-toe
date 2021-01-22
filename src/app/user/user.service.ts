import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {UserForm} from "./userForm";
import {BasicUser} from "./basicUser";
import {Ranking} from "../ranking/ranking";
import {SecurityService} from "../login/security.service";
import {Observable} from "rxjs";
import {FullUser} from "./fullUser";

@Injectable()
export class UserService {

  private readonly usersUrl;

  constructor(private http: HttpClient, private security: SecurityService) {
    this.usersUrl = 'http://localhost:8080/users';
  }

  public save(userForm: UserForm) {
    return this.http.post<BasicUser>(this.usersUrl, userForm);
  }

  public ranking() {
    return this.withAuthorization<Ranking>(this.usersUrl + "/ranking");
  }

  public getUserByUsername(username: String) {
    return this.withAuthorization<FullUser>(this.usersUrl + "/" + username);
  }

  update(id: number, userForm: UserForm) {
    if (this.security.user == undefined) {
      return new Observable<FullUser>()
    } else {
      let header = new HttpHeaders()
        .set("authorization", "Basic " + this.security.user.auth);
      return this.http.put<FullUser>(
        this.usersUrl + '/' + id,
        userForm,
        {'headers': header})
    }
  }


  private withAuthorization<T>(url: string) {
    if (this.security.user == undefined) {
      return new Observable<T>()
    } else {
      let header = new HttpHeaders()
        .set("authorization", "Basic " + this.security.user.auth);
      return this.http.get<T>(url, {'headers': header})
    }
  }


}


