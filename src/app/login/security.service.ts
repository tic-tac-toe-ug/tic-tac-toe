import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {map} from "rxjs/operators";
import {CookieService} from 'ng2-cookies';

@Injectable()
export class SecurityService {

  private static CookieName = "USER_DETAILS";

  user?: UserDetails

  private readonly url;

  constructor(private http: HttpClient, private cookies: CookieService) {
    this.url = 'http://localhost:8080';
  }

  loadCookie(): void {
    if (this.cookies.check(SecurityService.CookieName)) {
      this.user = JSON.parse(this.cookies.get(SecurityService.CookieName)) as UserDetails
    }
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
            data.details.sessionId,
            data.principal.password
          );
          this.cookies.set(SecurityService.CookieName, JSON.stringify(userDetails))
          this.user = userDetails
          return userDetails;
        })
      )
  }

  public logout() {
    return this.http.post<any>(this.url + '/logout', null)
      .pipe(
        map((data: any) => {
          this.user = undefined;
          this.cookies.delete(SecurityService.CookieName)
          return data;
        })
      )
  }

  public isLogged() {
    this.loadCookie();
    return this.user != undefined
  }

  public isAdmin(): boolean {
    this.loadCookie();
    return this.user == undefined ? false : this.user.roles.filter(role => role === 'ROLE_ADMIN').length > 0;
  }

}

export class UserDetails {
  id: number;
  username: string;
  email: string;
  roles: string[];
  sessionId: string;
  auth: string;

  constructor(id: number,
              username: string,
              email: string,
              roles: string[],
              sessionId: string,
              password: string) {
    this.id = id;
    this.username = username;
    this.email = email;
    this.roles = roles;
    this.sessionId = sessionId;
    this.auth = username + ":" + password;
  }
}

