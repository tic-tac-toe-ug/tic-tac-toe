import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {FullUser} from "./fullUser";
import {UserService} from "./user.service";
import {SecurityService} from "../login/security.service";

@Component({
  selector: 'show-user',
  templateUrl: './show-user.component.html',
  providers: [UserService]
})
export class ShowUserComponent implements OnInit {

  user: FullUser;
  securityService: SecurityService;

  constructor(private route: ActivatedRoute,
              private userService: UserService,
              securityService: SecurityService) {
    this.securityService = securityService;
  }

  ngOnInit(): void {
    let username = this.route.snapshot.paramMap.get('username') as string;
    this.userService.getUserByUsername(username)
      .subscribe(user => this.user = user);
  }
}
