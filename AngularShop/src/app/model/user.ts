import { Address } from "./address";

export interface User {
    userId?:number;
    firstname: string;
    lastname: string;
    company: string;
    email: string;
    phone: string;
    deliveryAddress: Address;
    billingAddress: Address;
    username: string;
  }