import { Injectable } from '@angular/core';
import {environment} from "../../environments/environment";
import {HttpClient} from "@angular/common/http";
import {User} from "../models/user.model";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private static readonly endpoint = `${environment.backendUrl}/user`;

  constructor(private http: HttpClient) { }

  create(user: User): Observable<User> {
    return this.http.post<User>(UserService.endpoint, {...user});
  }
}
