import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {UserService} from "../services/user.service";
import {Router} from "@angular/router";
import {User} from "../models/user.model";
import {take, tap} from "rxjs";

@Component({
  selector: 'app-create',
  templateUrl: './create.component.html',
  styleUrls: ['./create.component.scss']
})
export class CreateComponent implements OnInit {
  createForm: FormGroup;

  constructor(private formBuilder: FormBuilder,
              private router: Router,
              private userService: UserService) {
  }

  ngOnInit(): void {
    this.createForm = this.formBuilder.group({
      username: ['', Validators.required],
      password: ['', Validators.required],
      role: ['', Validators.required]
    })
  }

  onCreateClicked() {
    const user: User = {
      username: this.f['username'].value,
      password: this.f['password'].value,
      role: this.f['role'].value
    }
    this.userService.create(user).pipe(
      take(1),
      tap(console.log),
      tap(() => this.router.navigate(['/']))
    ).subscribe()
  }

  get f() {
    return this.createForm.controls;
  }

}
