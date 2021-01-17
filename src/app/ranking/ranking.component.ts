import {Component, Injectable, OnInit} from '@angular/core';
import {UserService} from "../user/user.service";
import {Ranking} from "../user/ranking";
import {RankingEntry} from "../user/ranking-entry";
import {AlertService} from "../alert-component/alert.service";

@Component({
  selector: 'app-ranking',
  templateUrl: './ranking.component.html',
  providers: [UserService]
})
@Injectable()
export class RankingComponent implements OnInit {

  entries: RankingEntry[]

  constructor(private userService: UserService,
              private alertService: AlertService) {
  }

  ngOnInit(): void {
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
