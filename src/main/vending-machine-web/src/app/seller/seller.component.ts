import {Component, OnDestroy, OnInit} from '@angular/core';
import {User} from "../models/user.model";
import {AuthService} from "../services/auth.service";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Product} from "../models/product.model";
import {ProductService} from "../services/product.service";
import {BehaviorSubject, map, Observable, Subscription, switchMap, take, tap} from "rxjs";

@Component({
  selector: 'app-seller',
  templateUrl: './seller.component.html',
  styleUrls: ['./seller.component.scss']
})
export class SellerComponent implements OnInit, OnDestroy {
  user?: User;
  products$: Observable<Product[]>;
  mode$: Observable<'add' | 'edit'>;
  selectedProductSubject: BehaviorSubject<Product | null>
  productForm: FormGroup;
  refreshSubject: BehaviorSubject<boolean>;
  displayedColumns = ['productName', 'cost', 'amountAvailable', 'sellerId', 'action'];
  subscriptions: Subscription;

  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private productService: ProductService) {
    this.selectedProductSubject = new BehaviorSubject<Product | null>(null);
    this.refreshSubject =new BehaviorSubject(true)
    this.subscriptions = new Subscription();
  }

  ngOnInit(): void {
    this.user = this.authService.getUser();
    this.productForm = this.formBuilder.group({
      productName: ['', Validators.required],
      amountAvailable: ['', Validators.required],
      cost: ['', Validators.required]
    });

    this.mode$ = this.selectedProductSubject.pipe(
      map(selectedProduct => selectedProduct ? 'edit' : 'add')
    );

    this.products$ = this.refreshSubject.pipe(
      switchMap(() => this.productService.list())
    );

    this.subscriptions.add(this.selectedProductSubject.pipe(
      tap(selectedProduct => {
        console.log(selectedProduct);
        if (selectedProduct) {
          this.productForm.patchValue({
            ...selectedProduct
          })
        } else {
          this.productForm.reset();
        }
      })
    ).subscribe());

  }

  ngOnDestroy() {
    this.subscriptions.unsubscribe();
  }

  onProductSubmit() {
    const product: Product = {
      productName: this.f['productName'].value,
      cost: this.f['cost'].value,
      amountAvailable: this.f['amountAvailable'].value
    }

    this.productService.create(product).pipe(
      take(1),
      tap(() => this.refreshSubject.next(true)),
      tap(() => this.selectedProductSubject.next(null)),
      tap(console.log)
    ).subscribe()
  }

  get f() {
    return this.productForm.controls;
  }

  onEditClicked(product: Product) {
    this.selectedProductSubject.next(product);
  }

  onDeleteClicked(product: Product) {
    this.productService.delete(product.productName).pipe(tap(() => this.refreshSubject.next(true))).subscribe();
  }
}
