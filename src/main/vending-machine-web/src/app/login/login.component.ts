import {Component, OnInit} from '@angular/core';
import {AuthService} from "../services/auth.service";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {take, tap} from "rxjs";
import {Router} from "@angular/router";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  loginForm: FormGroup;

  constructor(private formBuilder: FormBuilder,
              private router: Router,
              private authService: AuthService) {
  }

  ngOnInit(): void {
    this.loginForm = this.formBuilder.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    })
  }

  onLoginClicked() {
    this.authService.auth(this.f['username'].value, this.f['password'].value).pipe(
      take(1),
      tap(console.log),
      tap(user => user.role === 'ROLE_BUYER'? this.router.navigate(['/buyer']) : this.router.navigate(['/seller']))
    ).subscribe()
  }


  get f() {
    return this.loginForm.controls;
  }
}
