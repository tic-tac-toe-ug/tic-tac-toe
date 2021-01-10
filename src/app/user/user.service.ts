import {Component, Injectable, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {HttpClient} from "@angular/common/http";
import {UserForm} from "./userForm";
import {User} from "./user";

@Injectable()
export class UserService {

  private readonly usersUrl;

  constructor(private http: HttpClient) {
    this.usersUrl = 'http://localhost:8080/users';
  }

  public save(userForm: UserForm) {
    return this.http.post<User>(this.usersUrl, userForm);
  }

}


