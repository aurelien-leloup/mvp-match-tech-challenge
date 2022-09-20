import { Injectable } from '@angular/core';
import { environment } from "../../environments/environment";
import { HttpClient } from "@angular/common/http";
import { User } from "../models/user.model";
import { Observable } from "rxjs";
import { AuthService } from "./auth.service";

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private static readonly endpoint = `${environment.backendUrl}/user`;

  constructor(private http: HttpClient, private authService: AuthService) {
  }

  create(user: User): Observable<User> {
    return this.http.post<User>(UserService.endpoint, {...user});
  }

  read(username: string): Observable<User> {
    const headers = this.authService.getHeaders();
    return this.http.get<User>(UserService.endpoint + '/' + username, {headers});
  }
}
