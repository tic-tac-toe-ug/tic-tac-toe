import {ComponentFixture, TestBed} from '@angular/core/testing';

import {RegisterFormComponent} from './register-form.component';
import {ReactiveFormsModule} from "@angular/forms";

describe('RegisterFormComponent', () => {
  let component: RegisterFormComponent;
  let fixture: ComponentFixture<RegisterFormComponent>;
  let testUsername = "Test username"
  let testEmail = "test@email.com"
  let password = "password"

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ ReactiveFormsModule ],
      declarations: [RegisterFormComponent]
    })
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RegisterFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('form invalid when empty', () => {
    expect(component.form.valid).toBeFalsy();
    expect(component.f.login.errors).toEqual({"required": true})
    expect(component.f.email.errors).toEqual({"required": true})
    expect(component.f.password.errors).toEqual({"required": true})
    expect(component.f.repeatPassword.errors).toEqual({"required": true})
  });

  it('form valid when all passed', () => {
    component.form.controls['username'].setValue(testUsername);
    component.form.controls['email'].setValue(testEmail);
    component.form.controls['password'].setValue(password);
    component.form.controls['repeatPassword'].setValue(password);

    expect(component.form.valid).toBeTruthy();
  });

  it('form invalid when email in invalid form', () => {
    component.form.controls['username'].setValue(testUsername);
    component.form.controls['email'].setValue("asdfgg");
    component.form.controls['password'].setValue(password);
    component.form.controls['repeatPassword'].setValue(password);

    expect(component.form.valid).toBeFalse();
    expect(component.f.email.errors).toEqual({"email": true})
  });

  it('form invalid when password is too short', () => {
    component.form.controls['username'].setValue(testUsername);
    component.form.controls['email'].setValue(testEmail);
    component.form.controls['password'].setValue("abc");
    component.form.controls['repeatPassword'].setValue(password);

    expect(component.form.valid).toBeFalse();
    expect(component.f.password.errors).toEqual({"minlength": { "requiredLength": 8, "actualLength": 3 }})
  });

  it('form invalid when repeatPassword is too short', () => {
    component.form.controls['username'].setValue(testUsername);
    component.form.controls['email'].setValue(testEmail);
    component.form.controls['password'].setValue(password);
    component.form.controls['repeatPassword'].setValue("abc");

    expect(component.form.valid).toBeFalse();
    expect(component.f.repeatPassword.errors).toEqual({"minlength": { "requiredLength": 8, "actualLength": 3 }})
  });
});
