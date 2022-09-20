import {Injectable} from '@angular/core';
import {BehaviorSubject, map, Observable, Subject, tap} from "rxjs";
import {AuthUser, User} from "../models/user.model";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {environment} from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private static readonly endpoint = `${environment.backendUrl}/auth`;
  private userSubject: BehaviorSubject<AuthUser | null>;

  constructor(private http: HttpClient) {
    this.userSubject = new BehaviorSubject<AuthUser | null>(null);
  }

  auth(username: string, password: string): Observable<User> {
    return this.http.post<AuthUser>(AuthService.endpoint, {username, password}).pipe(
      tap(authUser => this.userSubject.next(authUser)),
      map(authUser => authUser.user),
    )
  }

  getUser(): User | undefined {
    return this.userSubject.getValue()?.user;
  }

  getAuthUser(): AuthUser | null {
    return this.userSubject.getValue();
  }

  getHeaders() {
    return new HttpHeaders({Authorization: 'Bearer ' + this.getToken()});
  }

  getToken() {
    return this.userSubject.getValue()?.jwtToken;
  }


}
