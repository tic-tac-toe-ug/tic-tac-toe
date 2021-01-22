import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {FullUser} from "./fullUser";
import {UserService} from "./user.service";
import {SecurityService} from "../login/security.service";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {UserForm} from "./userForm";
import {AlertService} from "../alert-component/alert.service";

@Component({
  selector: 'show-user',
  templateUrl: './show-user.component.html',
  providers: [UserService]
})
export class ShowUserComponent implements OnInit {

  user: FullUser;
  securityService: SecurityService;
  form: FormGroup;

  constructor(private route: ActivatedRoute,
              private userService: UserService,
              private formBuilder: FormBuilder,
              private alertService: AlertService,
              private router: Router,
              securityService: SecurityService) {
    this.securityService = securityService;
  }

  ngOnInit(): void {
    let username = this.route.snapshot.paramMap.get('username') as string;
    this.userService.getUserByUsername(username)
      .subscribe(user => {
        this.user = user
        this.form = this.formBuilder.group({
          email: [{value: user.email, disabled: !this.securityService.isAdmin()}, [Validators.required, Validators.email]],
          login: [{value: user.username, disabled: !this.securityService.isAdmin()}, [Validators.required, Validators.min(3), Validators.max(15)]],
          password: [{value: '', disabled: !(this.isSelf() || this.securityService.isAdmin())}, [Validators.required, Validators.minLength(8)]],
          repeatPassword: [{value: '', disabled: !(this.isSelf() || this.securityService.isAdmin())}, [Validators.required, Validators.minLength(8)]]
        })
      });
  }

  isSelf(): boolean {
    return this.user.id == this.securityService.user!.id
  }

  onSubmit() {
    console.log(this.form.getRawValue());
    this.userService.update(
      this.user.id,
      new UserForm(
        this.form.getRawValue().login,
        this.form.getRawValue().password,
        this.form.getRawValue().repeatPassword,
        this.form.getRawValue().email
      )
    ).subscribe(
      (user: any) => {
        console.log(user);
        this.alertService.success("Aktualizacja się powiodła!", {keepAfterRouteChange: true});
        this.router.navigateByUrl("/ranking")
      },
      (errorResponse: any) => {
        console.log(errorResponse)
        this.alertService.error(errorResponse.toString())
      }
    )
  }
}
