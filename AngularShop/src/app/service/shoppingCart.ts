export class ShoppingCart {
    private storageKey = 'shoppingCartItems';
    items: any[] = [];
  
    constructor() {
      this.loadCartItemsFromStorage();
    }
  
    addItem(item: any) {
      this.items.push(item);
      this.saveCartItemsToStorage();
    }
  
    removeItem(item: any) {
      const index = this.items.indexOf(item);
      if (index > -1) {
        this.items.splice(index, 1);
        this.saveCartItemsToStorage();
      }
    }
  
    clearCart() {
      this.items = [];
      this.saveCartItemsToStorage();
    }
  
    getCartItems() {
      return this.items;
    }

    public hasItems(){
      return this.items.length>0;
    }
  
    private loadCartItemsFromStorage() {
      const savedItems = localStorage.getItem(this.storageKey);
      if (savedItems) {
        this.items = JSON.parse(savedItems);
      }
    }
  
    private saveCartItemsToStorage() {
      localStorage.setItem(this.storageKey, JSON.stringify(this.items));
    }

    public findItemInCart(item: any): boolean {
      // Find the item in the cart based on your own criteria
      // For example, comparing item IDs
      const existingItem = this.items.find((cartItem) => cartItem.product.productId === item.product.productId);
      return !!existingItem; // Return true if an existing item is found, false otherwise
    }

    public removeItemById(id:number) {
      const index = this.items.findIndex(item => item.product.productId === id)
      if (index > -1) {
        this.items.splice(index, 1);
        this.saveCartItemsToStorage();
      }
    }

    public increaseQuantityById(itemId: number, quantity : number): void {
      const item = this.items.find(item => item.product.productId === itemId);
        this.removeItem(item);
        item.quantity += quantity;
        this.addItem(item);
        this.saveCartItemsToStorage();
      
    }
    
    public replaceQuantityById(itemId: number, quantity : number): void {
      this.loadCartItemsFromStorage();
      const item = this.items.find(item => item.product.productId === itemId);
      this.removeItem(item);
      item.quantity = quantity;
      this.addItem(item);
      this.saveCartItemsToStorage();
    }

    public mergeItems() {
      const mergedItems: any[] = [];
      this.items.forEach((item) => {
        const existingItem = mergedItems.find((mergedItem) => mergedItem.product.productId  === item.product.productId);
        if (existingItem) {
          existingItem.quantity += item.quantity;
        } else {
          mergedItems.push({...item});
        }
      });
      this.items = mergedItems;
      this.saveCartItemsToStorage();
    }

    public getSubtotal(){
      return this.getTotal() - this.getTotalTax();
    }
  
    public getTotalTax(){
      let sum = 0;
      for (let i = 0; i<this.items.length; i++){
        const taxPercent = this.items[i].product.tax;
        const price = this.items[i].product.price;
        let tax = price/100*taxPercent;
        tax = tax * this.items[i].quantity;
        sum += tax;
      }
      return sum
    }
  
    public getTotal(){
      let total = 0;
      for (let i = 0; i<this.items.length;i++){
        const item = this.items[i];
        const price = parseFloat(item.product.price) * item.quantity;
        total += price;
      }
      return total
    }

    public getOrder():any[]{
      this.loadCartItemsFromStorage();
      return this.items;
    }
  
  }
