import { Address } from "./address";

export interface User {
    userId?:number;
    erpId?: number;
    firstname: string;
    lastname: string;
    company: string;
    email: string;
    phone: string;
    deliveryAddress: Address;
    billingAddress: Address;
    username: string;
  }