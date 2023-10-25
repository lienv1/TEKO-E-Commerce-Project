export interface Product {
    productId: number,
    brand ?: string,
    category ?: string,
    description ?: string,
    discount : boolean,
    gtinPack ?: string,
    gtinUnit ?: string,
    lastModified ?: Date,
    origin ?: string,
    pack ?: number,
    price : number,
    productGroup ?: string,
    productName ?: string,
    searchIndex ?: string,
    stock ?: number,
    taxcode ?:number,
    weight ?: number
}