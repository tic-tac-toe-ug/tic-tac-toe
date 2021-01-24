import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {SecurityService} from "./security.service";
import {AlertService} from "../alert-component/alert.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html'
})
export class LoginComponent implements OnInit {
  form: FormGroup = new FormGroup({})
  loading = false;
  submitted = false;

  constructor(
    private formBuilder: FormBuilder,
    private alertService: AlertService,
    private router: Router,
    private security: SecurityService
  ) {

  }

  ngOnInit(): void {
    this.form = this.formBuilder.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  get f() {
    return this.form.controls;
  }

  onSubmit() {
    this.submitted = true;
    this.loading = true;

    // stop here if form is invalid
    if (this.form.invalid) {
      return;
    }
    let formData = new FormData();
    formData.append('username', this.form.getRawValue().username)
    formData.append('password', this.form.getRawValue().password)
    this.security.login(formData)
      .subscribe(
        (data) => {
          this.alertService.success("Zalogowano!", {keepAfterRouteChange: true});
          this.router.navigateByUrl("/join-game")
        },
        (errorResponse) => {
          this.alertService.error(errorResponse.error.errors)
          this.loading = false;
          this.submitted = false;
        })
  }

}
