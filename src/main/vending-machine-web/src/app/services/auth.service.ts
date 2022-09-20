import {Injectable} from '@angular/core';
import {map, Observable} from "rxjs";
import {AuthUser, User} from "../models/user.model";
import {HttpClient} from "@angular/common/http";
import {environment} from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private static readonly endpoint = `${environment.backendUrl}/auth`;

  constructor(private http: HttpClient) {
  }

  auth(username: string, password: string): Observable<User> {
    return this.http.post<AuthUser>(AuthService.endpoint, {username, password}).pipe(
      map(authUser => authUser.user)
    )
  }


}
