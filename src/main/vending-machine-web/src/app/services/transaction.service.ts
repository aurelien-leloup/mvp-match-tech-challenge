import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { environment } from "../../environments/environment";
import { AuthService } from "./auth.service";
import { ProductPurchase } from "../models/product-purchase.model";
import { Observable } from "rxjs";
import { TransactionResult } from "../models/transaction-result.model";

@Injectable({
  providedIn: 'root'
})
export class TransactionService {

  constructor(private http: HttpClient, private authService: AuthService) {
  }

  deposit(value: number): Observable<void> {
    const headers = this.authService.getHeaders();
    return this.http.put<void>(environment.backendUrl + '/deposit', {value}, {headers});
  }

  buy(productPurchases: ProductPurchase[]): Observable<TransactionResult> {
    const headers = this.authService.getHeaders();
    return this.http.post<TransactionResult>(environment.backendUrl + '/buy', {productPurchases}, {headers})
  }

  reset(): Observable<void> {
    const headers = this.authService.getHeaders();
    return this.http.post<void>(environment.backendUrl + '/reset', {}, {headers})

  }

}
