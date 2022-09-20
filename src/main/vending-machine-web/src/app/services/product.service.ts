import {Injectable} from '@angular/core';
import {environment} from "../../environments/environment";
import {HttpClient} from "@angular/common/http";
import {Product} from "../models/product.model";
import {Observable} from "rxjs";
import {AuthService} from "./auth.service";

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  private static readonly endpoint = `${environment.backendUrl}/product`;

  constructor(private http: HttpClient, private authService: AuthService) {
  }

  create(product: Product): Observable<Product> {
    const headers = this.authService.getHeaders();
    return this.http.post<Product>(ProductService.endpoint, {...product}, {headers});
  }

  list(): Observable<Product[]> {
    const headers = this.authService.getHeaders();
    return this.http.get<Product[]>(ProductService.endpoint, {headers});
  }

  update(product: Product): Observable<Product> {
    const headers = this.authService.getHeaders();
    return this.http.put<Product>(ProductService.endpoint, {...product}, {headers})
  }

  delete(productName: string): Observable<void> {
    const headers = this.authService.getHeaders();
    return this.http.delete<void>(ProductService.endpoint + '/' + productName, {headers});
  }

}
