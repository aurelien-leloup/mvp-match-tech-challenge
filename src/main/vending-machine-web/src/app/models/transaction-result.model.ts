import { ProductPurchase } from "./product-purchase.model";

export interface TransactionResult {
  totalSpent: number;
  productsPurchased: ProductPurchase[];
  change: Change[]
}

export interface Change {
  coinValue: number;
  quantity: number;
}
