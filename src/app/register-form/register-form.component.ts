import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {UserService} from "../user/user.service";
import {UserForm} from "../user/userForm";
import {BasicUser} from "../user/basicUser";
import {AlertService} from "../alert-component/alert.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-register-form',
  templateUrl: './register-form.component.html',
  providers: [UserService]
})
export class RegisterFormComponent implements OnInit {
  // @ts-ignore
  form: FormGroup;
  loading = false;
  submitted = false;

  constructor(
    private formBuilder: FormBuilder,
    private userService: UserService,
    private alertService: AlertService,
    private router: Router
  ) {

  }

  ngOnInit(): void {
    this.form = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
      login: ['', [Validators.required, Validators.min(3), Validators.max(15)]],
      password: ['', [Validators.required, Validators.minLength(8)]],
      repeatPassword: ['', [Validators.required, Validators.minLength(8)]]
    });
  }

  get f() {
    return this.form.controls;
  }

  onSubmit() {
    this.submitted = true;


    // stop here if form is invalid
    if (this.form.invalid) {
      return;
    }
    this.loading = true;
    this.userService.save(
      new UserForm(
        this.form.getRawValue().login,
        this.form.getRawValue().password,
        this.form.getRawValue().repeatPassword,
        this.form.getRawValue().email
      )
    ).subscribe(
      (user: BasicUser) => {
        this.alertService.success("Rejestracja się powiodła!", {keepAfterRouteChange: true});
        this.router.navigateByUrl("/login-form")
      },
      (errorResponse: any) => {
        this.alertService.error(errorResponse.toString())
        this.loading = false;
      }
    )

  }

}


