import { Address } from "./address";

export interface User {
    firstname: string;
    lastname: string;
    company: string;
    email: string;
    phone: string;
    deliveryAddress: Address;
    billingAddress: Address;
    username: string;
    business: boolean;
    deleted: boolean;
  }