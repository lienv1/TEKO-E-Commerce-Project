import { User } from "./user";
import { OrderDetail } from "./orderDetail";

export interface Order {
    orderId ?: number;
    user ?: User;
    orderDetails : OrderDetail[];
    comment ?: string;
    orderDate ?: Date;
    created ?: Date;
}