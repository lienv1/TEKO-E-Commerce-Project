import { Product } from "./product"

export interface OrderDetail{
    id : number,
    orderId ?: number,
    product : Product,
    quantity : number
}