import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {UserForm} from "./userForm";
import {User} from "./user";
import {Ranking} from "./ranking";

@Injectable()
export class UserService {

  private readonly usersUrl;

  constructor(private http: HttpClient) {
    this.usersUrl = 'http://localhost:8080/users';
  }

  public save(userForm: UserForm) {
    return this.http.post<User>(this.usersUrl, userForm);
  }

  public ranking() {
    return this.http.get<Ranking>(this.usersUrl + "/ranking")
  }

}


