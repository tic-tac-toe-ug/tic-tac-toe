export class UserForm {

  login: String;
  password: String;
  confirmPassword: String;
  email: String;

  constructor(login: String, password: String, confirmPassword: String, email: String) {
    this.login = login;
    this.password = password;
    this.confirmPassword = confirmPassword;
    this.email = email;
  }

}
