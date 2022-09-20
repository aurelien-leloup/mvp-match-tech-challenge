import { Component, OnInit } from '@angular/core';
import { User } from "../models/user.model";
import { AuthService } from "../services/auth.service";
import { ProductService } from "../services/product.service";
import { BehaviorSubject, Observable, switchMap, take, tap } from "rxjs";
import { Product } from "../models/product.model";
import { ProductPurchase } from "../models/product-purchase.model";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { TransactionService } from "../services/transaction.service";
import { UserService } from "../services/user.service";
import { MatDialog } from "@angular/material/dialog";
import { TransactionDialogComponent } from "../transaction-dialog/transaction-dialog.component";

@Component({
  selector: 'app-buyer',
  templateUrl: './buyer.component.html',
  styleUrls: ['./buyer.component.scss']
})
export class BuyerComponent implements OnInit {
  username?: string;
  productsPurchased: ProductPurchase[] = [];
  user$: Observable<User>;
  products$: Observable<Product[]>;
  refreshProductsSubject: BehaviorSubject<boolean>;
  refreshUserSubject: BehaviorSubject<boolean>;
  displayedColumns = ['productName', 'cost', 'amountAvailable', 'sellerId', 'action'];
  depositForm: FormGroup;

  constructor(
    private dialog: MatDialog,
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private productService: ProductService,
    private transactionService: TransactionService,
    private userService: UserService
  ) {
    this.refreshProductsSubject = new BehaviorSubject(true);
    this.refreshUserSubject = new BehaviorSubject<boolean>(true);
  }

  ngOnInit(): void {
    this.username = this.authService.getUser()?.username;
    this.depositForm = this.formBuilder.group({
      deposit: [5, Validators.required]
    })

    this.user$ = this.refreshUserSubject.pipe(
      switchMap(() => this.userService.read(this.username ?? ''))
    )

    this.products$ = this.refreshProductsSubject.pipe(
      switchMap(() => this.productService.list())
    );
  }

  onPurchaseIncremented(productName: string) {
    if (this.productsPurchased.find(productPurchase => productPurchase.productId === productName)) {
      this.productsPurchased = this.productsPurchased.map(
        productPurchase => (productPurchase.productId === productName) ? {
          ...productPurchase,
          quantity: productPurchase.quantity + 1
        } : productPurchase)
    } else {
      this.productsPurchased.push({
        productId: productName,
        quantity: 1
      })
    }
  }

  onResetClicked() {
    this.transactionService.reset().pipe(
      take(1),
      tap(() => this.refreshUserSubject.next(true))
    ).subscribe();
  }

  onDepositClicked() {
    const deposit = this.f['deposit'].value;
    this.transactionService.deposit(deposit).pipe(
      take(1),
      tap(() => this.refreshUserSubject.next(true))
    ).subscribe();
  }

  onBuyClicked() {
    this.transactionService.buy(this.productsPurchased).pipe(
      take(1),
      switchMap(transactionResult => this.dialog.open(TransactionDialogComponent, {
        height: '400px',
        width: '600px',
        data: {result: transactionResult}
      }).afterClosed()),
      tap(() => this.productsPurchased = []),
      tap(() => this.refreshUserSubject.next(true)),
      tap(() => this.refreshProductsSubject.next(true))
    ).subscribe()

  }

  get f() {
    return this.depositForm.controls;
  }
}
