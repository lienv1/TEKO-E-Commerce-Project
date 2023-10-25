import { HttpErrorResponse } from '@angular/common/http';
import { Component, ElementRef, HostListener, OnInit, ViewChild } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { ActivatedRoute, Router, NavigationExtras } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { TranslateService } from '@ngx-translate/core';
import { KeycloakService } from 'keycloak-angular';
import { CartItem } from '../model/cartItem';
import { FunctionModel } from '../model/functionModel';
import { Product } from '../model/product';
import { ProductCategory } from '../model/productCategory';
import { ProductService } from '../service/product.service';
import { ShoppingCart } from '../service/shoppingCart';
import { CustomModalComponent } from '../modal/custom-modal/custom-modal.component';

@Component({
  selector: 'app-shop',
  templateUrl: './shop.component.html',
  styleUrls: ['./shop.component.scss']
})
export class ShopComponent implements OnInit {

  @ViewChild('productList') productListElement !: ElementRef<HTMLElement>
  @ViewChild('inputID') searchInput !: ElementRef<HTMLInputElement>
  @ViewChild("CustomModalComponent") customModalComponent !: CustomModalComponent

  //page variables
  page: number = 1;

  //Filter
  //Category
  category: string = "";
  group: string = "";
  keywords: string = "";
  favourite: string = "";

  public productCategories: ProductCategory[] = []
  selectedBrands: string[] = [];

  //Brand variables
  brands: string[] = [];

  //Favourite
  logged = false;
  username ?: string;

  //Products
  public allProducts: Product[] = []
  public currentProducts: Product[] = []

  constructor(
    private title:Title,
    private route: ActivatedRoute, 
    private router: Router, 
    private translate: TranslateService, 
    private productService: ProductService, 
    private shoppingCart: ShoppingCart,
    private keycloakService: KeycloakService,
    private modalService: NgbModal) { }

  ngOnInit(): void {
    this.initCategories();
    this.initProducts();
    this.getPageParam();
    this.title.setTitle("Shop")
    this.isLogged();
  }

  //For loading API services
  ngAfterViewInit(): void {
    this.handleParam();
  }


  @HostListener('window:popstate', ['$event'])
  onPopState(event: any) {
    // Function to be triggered when the "forward" or "back" button is clicked
    console.log('Forward or Back button clicked');
  }

  //INIT
  public initProducts() {
    this.productService.getProducts().subscribe(
      (response: Product[]) => {
        this.allProducts = response
        this.currentProducts = this.allProducts
        this.handleParam();
        this.getBrands();
      },
      (error: HttpErrorResponse) => {
        alert(error.message)
      }
    )
  }

  public initCategories() {
    this.productService.getCategories().subscribe(
      (response: ProductCategory[]) => {
        this.productCategories = response
      },
      (error: HttpErrorResponse) => {
        alert(error.message)
      }
    )
  }

  isLogged() {
    this.keycloakService.isLoggedIn().then(
      (logged) => {
        this.logged = logged
        if (logged)
        this.keycloakService.loadUserProfile().then(
          (user) => {
            this.username = user.username;
          }
        )
      }
    )
  }

  //INIT SECTION END
  collectAllParams() {
    this.route.queryParams.subscribe(params => {
      const categoryParam = params['category'];
      this.category = categoryParam || '';

      const groupParam = params['group'];
      this.group = groupParam || '';

      const keywordsParam = params['keywords'];
      this.keywords = keywordsParam || '';

      const favouriteParam = params['favourite'];
      this.favourite = favouriteParam || '';

      const brands: string = params['brands'];
      if (brands != null) {
        this.selectedBrands = brands.split("¿") || [];
      }
    });
  }

  handleParam() {
    this.scroll(this.productListElement.nativeElement);
    this.setPageParam();
    this.collectAllParams();
    if (!this.isNullOrEmpty(this.category) && !this.isNullOrEmpty(this.group)) {
      this.functionForCategoryGroup(this.category, this.group)
      this.applyBrandFilterAccordingToCurrentList();
      return;
    }
    else if (!this.isNullOrEmpty(this.keywords)) {
      this.searchIt(this.keywords.replace("¿", " "))
      this.applyBrandFilterAccordingToCurrentList();
      return;
    }
    else if (!this.isNullOrEmpty(this.favourite)){
      if (this.username != null)
        this.getFavourite(this.username)
      return;
    }
    if (this.selectedBrands.length > 0 && this.selectedBrands[0] != null) {
      this.applyBrandFilter();
      return;
    }
    else {
      this.currentProducts = this.allProducts
      return
    }
  }

  //CATEGORY SECTION
  functionForCategoryGroupBt(category: string, group: string) {
    this.uncheckAllCheckboxes();
    this.removeAllParams().then(
      () => {
        this.page = 1
        const navigationExtras: NavigationExtras = {
          queryParams: { category: category, group: group },
          queryParamsHandling: 'merge'
        };
        this.router.navigate([], navigationExtras);
        this.functionForCategoryGroup(category, group)
      }
    )
  }
  functionForCategoryGroup(category: string, group: string) {
    let products: Product[] = []
    for (let i = 0; i < this.allProducts.length; i++) {
      let currentProduct = this.allProducts[i]
      if (currentProduct.category == category && currentProduct.productGroup == group) {
        products.push(currentProduct)
      }
    }
    this.currentProducts = products
    this.getBrandsAccordingToCurrentProducts()
  }
  //CATEGORY SECTION END

  //SEARCH SECTION
  public searchItBt(keyword: string) {
    this.uncheckAllCheckboxes();
    this.removeAllParams().then(
      () => {
        this.page = 1
        const navigationExtras: NavigationExtras = {
          queryParams: { keywords: keyword.replace(" ", "¿") },
          queryParamsHandling: 'merge'
        };
        this.router.navigate([], navigationExtras);
        this.searchIt(keyword)
      }
    )
  }

  public searchIt(keywords: string) {
    const keywordsArray = this.getKeywords(keywords)
    let products: Product[] = []
    for (let i = 0; i < this.allProducts.length; i++) {
      let product = this.allProducts[i]
      if (this.productContainAllOfStringArray(product, keywordsArray)) {
        products.push(product)
      }
    }
    this.currentProducts = products
    this.getBrandsAccordingToCurrentProducts()
  }

  //FAVOURITE SECTION
  public getFavouriteBt(){
    this.uncheckAllCheckboxes();
    this.removeAllParams().then (
      () => {
        this.page = 1;
        const navigationExtras: NavigationExtras = {
          queryParams: { favourite: this.username},
          queryParamsHandling: 'merge'
        };
        this.router.navigate([], navigationExtras);
        if (this.username != null)
          this.getFavourite(this.username);
      }
    )
  }

  public getFavourite(username:string){
    this.productService.getFavourites(username).subscribe(
      (response: Product[]) => {
        this.currentProducts = response;
      },
      (error:HttpErrorResponse) => {
        let modal = this.customModalComponent;
        modal.title = "WARNING";
        modal.message = error.message;
        modal.colorTitle = "red";
        modal.functionModels = [];
        this.openModal(modal,false);
      }
    )
  }
  //FAVOURITE SECTION END

  //turn inputs to string array and handle quotation mark
  public getKeywords(searchInput: string): string[] {
    const keywords: string[] = [];
    let currentKeyword = '';

    // Flag indicating whether we're currently inside a quoted phrase
    let insideQuote = false;

    for (let i = 0; i < searchInput.length; i++) {
      const char = searchInput[i];

      if (char === '"') {
        // Toggle the insideQuote flag
        insideQuote = !insideQuote;

        if (!insideQuote && currentKeyword) {
          // If we've just finished a quoted phrase, add it to the keywords array
          keywords.push(currentKeyword.trim());
          currentKeyword = '';
        }
      } else if (char === ' ' && !insideQuote) {
        // If we encounter a space and we're not inside a quoted phrase, add the current keyword to the array
        if (currentKeyword) {
          keywords.push(currentKeyword.trim());
          currentKeyword = '';
        }
      } else {
        // Append the current character to the current keyword
        currentKeyword += char;
      }
    }
    // Add the final keyword to the array if there is one
    if (currentKeyword) {
      keywords.push(currentKeyword.trim());
    }
    return keywords;
  }

  public productContainAllOfStringArray(product: Product, keywords: string[]): boolean {
    for (let i = 0; i < keywords.length; i++) {
      let keyword = keywords[i]
      if (!product.searchIndex?.toLowerCase().includes(keyword.toLowerCase())) {
        return false
      }
    }
    return true
  }
  //SEARCH SECTION ENDS

  //BRAND SECTION
  getBrands() {
    let stringSet: Set<string> = new Set();
    for (let i = 0; i < this.allProducts.length; i++) {
      let product: Product = this.allProducts[i]
      stringSet.add(product.brand!)
    }
    this.brands = Array.from(stringSet);
  }

  getBrandsAccordingToCurrentProducts() {
    let stringSet: Set<string> = new Set();
    for (let i = 0; i < this.currentProducts.length; i++) {
      let product: Product = this.currentProducts[i]
      stringSet.add(product.brand!)
    }
    this.brands = Array.from(stringSet);
  }

  onCheckboxChange(brand: string, event: Event) {
    const target = event.target as HTMLInputElement;
    const isChecked = target.checked;

    if (isChecked) {
      this.selectedBrands.push(brand);
    } else {
      const index = this.selectedBrands.indexOf(brand);
      if (index > -1) {
        this.selectedBrands.splice(index, 1);
      }
    }
    this.updateUrlWithSelectedBrands();
  }

  updateUrlWithSelectedBrands() {
    if (this.selectedBrands.length > 0 || !this.isNullOrEmpty(this.selectedBrands[0])) {
      this.router.navigate([], {
        queryParams: { brands: this.selectedBrands.join('¿') },
        queryParamsHandling: 'merge',
      })
      return;
    }
    else {
      this.router.navigate([], {
        queryParams: { brands: null },
        queryParamsHandling: 'merge'
      })
    }
  }

  public applyBrandFilter() {
    let products: Product[] = [];
    for (let i = 0; i < this.allProducts.length; i++) {
      let product = this.allProducts[i]
      if (this.stringContainArray(product.brand!, this.selectedBrands)) {
        products.push(product)
      }
    }
    this.currentProducts = products
  }

  public applyBrandFilterAccordingToCurrentList() {
    if (this.selectedBrands.length < 1) {
      return;
    }
    let products: Product[] = [];
    for (let i = 0; i < this.currentProducts.length; i++) {
      let product = this.currentProducts[i]
      if (this.stringContainArray(product.brand!, this.selectedBrands)) {
        products.push(product)
      }
    }
    this.currentProducts = products
  }

  public uncheckAllCheckboxes() {
    const checkboxes: HTMLInputElement[] = Array.from(document.querySelectorAll('.form-check-input'));
    checkboxes.forEach((checkbox: HTMLInputElement) => {
      checkbox.checked = false;
    });
    this.router.navigate([], {
      queryParams: { brands: null },
      queryParamsHandling: 'merge'
    })
    this.selectedBrands = [];
  }

  //BRAND SECTION END

  //PAGE SECTION
  public getPageParam() {
    this.route.queryParams.subscribe(
      params => {
        const param = params['page'];
        let result = 1;
        if (param != null) {
          result = Number.parseInt(param);
        }
        this.page = result;
      }
    )
  }
  public setPageParam() {
    this.router.navigate([], {
      queryParams: { page: this.page },
      queryParamsHandling: 'merge'
    })
    this.scroll(this.productListElement.nativeElement);
  }
  //PAGE SECTION ENDS

  scroll(el: HTMLElement) {
    el.scrollIntoView({ behavior: 'smooth', block: 'start' });
  }

  public getTranslation(str: string) {
    return this.translate.instant(str);
  }

  //tools
  public isNullOrEmpty(value: any): boolean {
    return value === null || value === ""
  }

  public hasCarton(product:Product): boolean {
    return product?.pack !== 1
  }

  removeAllParams() {
    return new Promise<void>((resolve, reject) => {
      const navigationExtras: NavigationExtras = {
        queryParams: {},
        queryParamsHandling: ''
      };

      this.router.navigate([], navigationExtras)
        .then(() => {
          resolve();
        })
        .catch(error => {
          reject(error);
        });
    });
  }

  public stringContainArray(str: string, keywords: string[]): boolean {
    for (let i = 0; i < keywords.length; i++) {
      let keyword = keywords[i]
      if (str === keyword) {
        return true
      }
    }
    return false
  }

  public addItemToCart(product: Product) {
    const quantityInput = <HTMLInputElement>document.getElementById(`quantity-${product.productId}`)
    let quantity = parseFloat(quantityInput.value)
    if (quantity < 1) {
      return
    }
    const quantitySelector = <HTMLSelectElement>document.getElementById(`quantityType-${product.productId}`)
    const selector = quantitySelector.value
    if (selector == "carton") {
      quantity *= product.pack!
    }
    const cartItem: CartItem = {
      product,
      quantity
    }
    //If car already has item
    if (this.shoppingCart.findItemInCart(cartItem)) {
      //Increase function
      let increaseFunction: () => void = () => this.increaseQuantity(cartItem.product, quantity)
      const increaseName = this.translate.instant("INCREASE");
      const increaseModel: FunctionModel = {
        message: increaseName,
        foo: increaseFunction,
      }
      //Replace function
      let replaceFunction: () => void = () => this.replaceQuantity(cartItem.product, quantity);
      const replaceName: string = this.translate.instant("REPLACE")
      const replaceModel: FunctionModel = {
        message: replaceName,
        foo: replaceFunction
      }
      //Modal setting
      const message: string = cartItem.product.productId + " " + cartItem.product.productName + " - " + this.translate.instant("ITEM EXISTS");
      const title: string = this.translate.instant("WARNING");
      const color: string = "red";
      let modal = this.customModalComponent;
      modal.title = title;
      modal.message = message;
      modal.colorTitle = color;
      modal.functionModels = [replaceModel, increaseModel];
      this.openModal(modal, false);
      return;
    }

    this.shoppingCart.addItem(cartItem)
    let modal = this.customModalComponent;
    modal.message = product.productId + " " + product.productName + ": " + quantity;
    modal.title = this.translate.instant("ITEM ADDED SUCCESS");
    modal.colorTitle = "inherit"
    this.openModal(modal, true);
    return;
  }

  public increaseQuantity(product: Product, quantity: number) {
    if (quantity == null) {
      console.log("No quantity")
      return
    }

    if (quantity > 0) {
      if (product != null) {
        this.shoppingCart.increaseQuantityById(product.productId, quantity)
        console.log("Quantity increased")
      }
    }
  }

  public replaceQuantity(product: Product, quantity: number) {
    if (quantity == null) {
      console.log("No quantity")
      return
    }

    if (quantity > 0) {
      if (product != null)
        this.shoppingCart.replaceQuantityById(product.productId, quantity)
    }
  }

  //Popup section
  public openModal(modal: any, autoclose: boolean) {
    let modalRef = this.modalService.open(modal.myModal);
    if (autoclose) {
      setTimeout(() => {
        modalRef.dismiss();
      }, 3000);
    }
  }
  //Popup section end

  //THEME SECTION
  public isDarkMode(){
    const theme = localStorage.getItem("Theme");
    return theme === "dark"
  }
  //THEME SECTION END

}
