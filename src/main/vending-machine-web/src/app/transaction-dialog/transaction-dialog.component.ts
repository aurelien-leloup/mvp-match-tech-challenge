import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA } from "@angular/material/dialog";
import { TransactionResult } from "../models/transaction-result.model";

@Component({
  selector: 'app-transaction-dialog',
  templateUrl: './transaction-dialog.component.html',
  styleUrls: ['./transaction-dialog.component.scss']
})
export class TransactionDialogComponent implements OnInit {

  constructor(@Inject(MAT_DIALOG_DATA) public data: {result: TransactionResult}) { }

  ngOnInit(): void {
  }

}
