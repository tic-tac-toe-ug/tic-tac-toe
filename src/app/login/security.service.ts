import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {map} from "rxjs/operators";

@Injectable()
export class SecurityService {

  user?: UserDetails

  private readonly url;

  constructor(private http: HttpClient) {
    this.url = 'http://localhost:8080';
  }

  public login(formData: FormData) {
    return this.http.post<any>(this.url + '/login', formData)
      .pipe(
        map((data: any) => {
          let userDetails = new UserDetails(
            data.principal.id,
            data.name,
            data.principal.email,
            data.authorities.map((e: any) => e.authority),
            data.details.sessionId
          );
          this.user = userDetails
          return userDetails;
        })
      )
  }

  public isLogged() {
    return this.user != undefined
  }

}

export class UserDetails {
  id: number;
  username: string;
  email: string;
  roles: string[];
  sessionId: string;


  constructor(id: number, username: string, email: string, roles: string[], sessionId: string) {
    this.id = id;
    this.username = username;
    this.email = email;
    this.roles = roles;
    this.sessionId = sessionId;
  }
}

