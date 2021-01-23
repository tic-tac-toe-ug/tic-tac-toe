import {Component, Injectable, OnInit} from '@angular/core';
import {UserService} from "../user/user.service";
import {Ranking} from "./ranking";
import {RankingEntry} from "./ranking-entry";
import {AlertService} from "../alert-component/alert.service";
import {SecurityService} from "../login/security.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-ranking',
  templateUrl: './ranking.component.html',
  providers: [UserService]
})
@Injectable()
export class RankingComponent implements OnInit {

  entries: RankingEntry[]
  securityService: SecurityService

  constructor(private userService: UserService,
              securityService: SecurityService,
              private alertService: AlertService,
              private router: Router) {
    this.securityService = securityService;
  }

  ngOnInit(): void {
    this.refreshRanking();
  }

  resetRanking() {
    this.userService.resetRanking()
      .subscribe(
        (ranking: Ranking) => {
          this.entries = ranking.ranking
        },
        (errorResponse: any) => {
          errorResponse.error.errors
            .map((x: any) => x.defaultMessage)
            .forEach((message: string) => this.alertService.error(message))
        })
  }

  deleteUser(login: String) {
    this.userService.deleteUser(login)
      .subscribe(
        (something: any) => {
          this.refreshRanking();
        },
        (errorResponse: any) => {
          errorResponse.error.errors
            .map((x: any) => x.defaultMessage)
            .forEach((message: string) => this.alertService.error(message))
        })
  }

  private refreshRanking() {
    this.userService.ranking().subscribe(
      (ranking: Ranking) => {
        this.entries = ranking.ranking
      },
      (errorResponse: any) => {
        errorResponse.error.errors
          .map((x: any) => x.defaultMessage)
          .forEach((message: string) => this.alertService.error(message))
      })
  }
}
