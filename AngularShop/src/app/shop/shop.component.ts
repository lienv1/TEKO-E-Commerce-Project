import { HttpErrorResponse, HttpParams } from '@angular/common/http';
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
import { environment } from 'src/environments/environment';
import { Filters } from '../model/filters';
import { ExtendedModalService } from '../service/extendedModalService';

export enum Sorter {
  productId = 'Product ID',
  productName = 'Product Name',
  /*price = 'Price',*/
  lastModified = 'Date'
}

@Component({
  selector: 'app-shop',
  templateUrl: './shop.component.html',
  styleUrls: ['./shop.component.scss']
})
export class ShopComponent implements OnInit {

  @ViewChild('productList') productListElement !: ElementRef<HTMLElement>
  @ViewChild('inputID') searchInput !: ElementRef<HTMLInputElement>
  @ViewChild("CustomModalComponent") customModalComponent !: CustomModalComponent

  //URLs
  fileServer = environment.fileServerAPI

  //page variables
  page: number = 1;
  maxItems: number = 1;

  //Param
  searchParam !: string;
  categoryParam !: string;
  subcategoryParam !: string;
  brandParam !: string;
  originParam !: string;
  favoriteParam !: string;

  //Filter
  sortBy: string = "lastModified"
  filters?: Filters
  brandSet = new Set();
  originSet = new Set();
  sortOptions = Object.entries(Sorter).map(([key, value]) => ({ key, value }));

  //Category
  public categories: ProductCategory[] = [];

  //Favourite
  logged = false;
  username?: string;

  //Products
  public allProducts: Product[] = []
  public currentProducts: Product[] = []

  //Images
  fallbackImageLoaded = false; //Prevent infinite loop

  //Popup
  private extendedModalService: ExtendedModalService;

  constructor(
    private title: Title,
    private route: ActivatedRoute,
    private router: Router,
    private translate: TranslateService,
    private productService: ProductService,
    private shoppingCart: ShoppingCart,
    private keycloakService: KeycloakService,
    private modalService: NgbModal
  ) {
    this.extendedModalService = new ExtendedModalService(modalService);
  }

  ngOnInit(): void {

    this.translate.get('SHOP').subscribe(element => { this.title.setTitle(element) })
    this.isLogged().then(() =>
      this.initParam()
    );
    this.initCategories();
  }

  /*ngAfterViewInit(){
    this.scroll(this.container.nativeElement)
  }*/

  @HostListener('window:popstate', ['$event'])
  onPopState(event: any) {
    // Function to be triggered when the "forward" or "back" button is clicked
    console.log('Forward or Back button clicked');
  }

  //INIT
  //Replace InitParam()
  public initParam() {
    this.route.queryParamMap.subscribe(queryParams => {
      const searchParam = queryParams.get('search');
      const favoriteParam = queryParams.get('favorite');
      const categoryParam = queryParams.get('category');
      const subcategoryParam = queryParams.get('subcategory');
      const brandParam = queryParams.get('brand');
      const originParam = queryParams.get('origin');
      const pageParam = queryParams.get('page');
      const sortParam = queryParams.get('sort');

      this.page = pageParam === null ? 1 : Number.parseInt(pageParam);
      this.brandParam = brandParam === null ? "" : brandParam;
      this.originParam = originParam === null ? "" : originParam;
      this.searchParam = searchParam === null ? "" : searchParam;
      this.favoriteParam = favoriteParam === null ? "" : favoriteParam;
      this.categoryParam = categoryParam === null ? "" : categoryParam;
      this.subcategoryParam = subcategoryParam === null ? "" : subcategoryParam;
      this.sortBy = sortParam === null ? "" : sortParam;
      this.initProducts();
    });
  }

  public initProducts() {
    let params = new HttpParams();
    params = this.appendFilterParam(params);
    params = this.appendPageParam(params);
    params = this.appendSortParam(params);
    this.productService.getProductByFilter(params).subscribe({
      next: (response) => { this.handleResponse(response); this.initFilter(params), this.scroll(this.productListElement.nativeElement) },
      error: (error: HttpErrorResponse) => this.handleError(error)
    })
  }


  public initCategories() {
    this.productService.getCategories().subscribe({
      next: (response: ProductCategory[]) => this.categories = response,
      error: (error: HttpErrorResponse) => this.handleError(error)
    })
  }

  public initFilter(param: HttpParams) {
    this.productService.getFilters(param).subscribe({
      next: (response) => { this.filters = response },
      error: (error: HttpErrorResponse) => { this.handleError(error) }
    });
  }

  isLogged(): Promise<void> {
    return this.keycloakService.isLoggedIn().then(
      (logged) => {
        this.logged = logged
        if (logged)
          return this.keycloakService.loadUserProfile().then(
            (user) => {
              this.username = user.username;
            }
          )
        else {//this.router.navigateByUrl("/login"); no longer used, because AuthGuard has been implemented
          return;}
      },
      () => { return }
    )
  }

  //INIT SECTION END

  //FILTER BAR SECTION

  onOriginChange(event: any, origin: string): void {
    const isChecked = event.target.checked;
    // Do something with the checked state and the origin value
    if (isChecked) {
      // Code to handle the checkbox being checked
      this.originSet.add((origin));
    } else {
      // Code to handle the checkbox being unchecked
      this.originSet.delete((origin));
    }
    this.appendOriginParam();
  }

  onBrandChange(event: any, brand: string): void {
    const isChecked = event.target.checked;
    // Do something with the checked state and the brand value
    if (isChecked) {
      // Code to handle the checkbox being checked
      this.brandSet.add((brand));
    } else {
      // Code to handle the checkbox being unchecked
      this.brandSet.delete((brand));
    }
    this.appendBrandParam();
  }

  appendBrandParam() {
    if (!this.brandSet || this.brandSet.size < 1) {
      this.removeQueryParam("brand");
      this.brandParam = "";
      return;
    }
    this.brandParam = Array.from(this.brandSet).join("¿");
    this.router.navigate([], {
      queryParams: { brand: this.brandParam, page: 1 },
      queryParamsHandling: 'merge'
    })
  }

  appendOriginParam() {
    if (!this.originSet || this.originSet.size < 1) {
      this.removeQueryParam("origin");
      this.originParam = "";
      return;
    }
    this.originParam = Array.from(this.originSet).join("¿");
    this.router.navigate([], {
      queryParams: { origin: this.originParam, page: 1 },
      queryParamsHandling: 'merge'
    })
  }

  removeQueryParam(paramKey: string) {
    this.router.navigate([], {
      queryParams: { [paramKey]: null },
      queryParamsHandling: 'merge'
    })
  }

  brandIsChecked(brand: string) {
    if (!this.brandParam)
      return false;
    this.brandSet = new Set(this.brandParam.split('¿'));
    return this.brandSet.has(brand)
  }

  originIsChecked(origin: string) {
    if (!this.originParam)
      return false;
    this.originSet = new Set(this.originParam.split('¿'));
    return this.originSet.has(origin);
  }

  onSortChange(event: Event, orderByAsc: boolean) {
    let orderBy: string = orderByAsc ? 'asc' : 'desc';
    console.log(orderByAsc + " " + orderBy)
    this.sortBy = (event.target as HTMLInputElement).value + "," + orderBy;
    this.router.navigate([], {
      queryParams: {
        sort: this.sortBy
      },
      queryParamsHandling: 'merge'
    })
  }

  //FILTER BAR SECTION END

  appendPageParam(params: HttpParams) {
    if (this.page !== 1) {
      params = params.append("page", this.page - 1);
    }
    return params;
  }

  appendSortParam(params: HttpParams) {
    if (this.sortBy)
      params = params.append("sort", this.sortBy)
    return params;
  }

  appendFilterParam(params: HttpParams) {
    if (this.categoryParam)
      params = params.append("category", this.categoryParam);
    if (this.subcategoryParam)
      params = params.append("subcategory", this.subcategoryParam);
    if (this.brandParam)
      params = params.append("brand", this.brandParam);
    if (this.originParam)
      params = params.append("origin", this.originParam);
    if (this.favoriteParam && this.username)
      params = params.append("favorite", this.username);
    if (this.searchParam)
      params = params.append("keywords", this.searchParam);
    return params
  }

  handleResponse(response: any) {
    this.currentProducts = response.content;
    this.maxItems = response.totalElements;
  }
  handleError(error: HttpErrorResponse) {
    if (error.status === 404) {
      this.redirectToProfilEdit();
      return;
    }
    this.extendedModalService.popup(this.customModalComponent, "ERROR", error.message, "red", [], false)
  }

  //SEARCH SECTION
  public searchItBt() {
    let keywords = this.searchInput.nativeElement.value;
    //Remove all double or more empty spaces
    keywords = keywords.replace(/\s{2,}/g, ' ');
    this.router.navigate([], {
      queryParams: { search: keywords.replace(" ", "¿") }
    })
    this.scroll(this.productListElement.nativeElement);
  }

  //FAVOURITE SECTION

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

  //SEARCH SECTION ENDS


  //PAGE SECTION
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

  public hasCarton(product: Product): boolean {
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

  //CART SECTION

  public addItemToCart(product: Product, quantityInput: HTMLInputElement, quantitySelector: HTMLSelectElement) {
    let quantity = parseFloat(quantityInput.value)
    if (isNaN(quantity) || quantity < 1) {
      this.extendedModalService.popup(this.customModalComponent, this.translate.instant('QUANTITY REQUIRED'), this.translate.instant('QUANTITY REQUIRED TEXT'), "red");
      return
    }

    /* if (product.stock != null && product.stock < quantity){
      this.extendedModalService.popup(this.customModalComponent,this.translate.instant('STOCK LIMIT EXCEED'),this.translate.instant('STOCK LIMIT EXCEED TEXT'),"red");
      return;
    }*/

    const selector = quantitySelector.value
    if (selector === "carton") {
      quantity *= product.pack!
    }
    const cartItem: CartItem = {
      product,
      quantity
    }

    if (product.deleted || !product.stock || product.stock < 1) {
      this.extendedModalService.popup(this.customModalComponent, this.translate.instant('OUT OF STOCK'), this.translate.instant('NOT AVAILABLE'), "red");
      return;
    }

    //If car already has item
    if (this.shoppingCart.findItemInCart(cartItem)) {
      //Increase function
      let increaseFunction: () => void = () => this.increaseQuantity(cartItem.product, quantity)
      const increaseName = this.translate.instant("INCREASE");
      const increaseModel: FunctionModel = {
        buttonText: increaseName,
        foo: increaseFunction,
      }
      //Replace function
      let replaceFunction: () => void = () => this.replaceQuantity(cartItem.product, quantity);
      const replaceName: string = this.translate.instant("REPLACE")
      const replaceModel: FunctionModel = {
        buttonText: replaceName,
        foo: replaceFunction
      }
      //Modal setting

      const message: string = cartItem.product.productId + " " + cartItem.product.productName + " - " + this.translate.instant("ITEM EXISTS");
      const title: string = this.translate.instant("WARNING");
      const color: string = "red";

      this.extendedModalService.popup(
        this.customModalComponent,
        title,
        message,
        color,
        [replaceModel, increaseModel],
        false
      );
      return;
    }
    this.shoppingCart.addItem(cartItem)
    this.extendedModalService.popup(
      this.customModalComponent,
      this.translate.instant("ITEM ADDED SUCCESS"),
      product.productId + " " + product.productName + ": " + quantity,
      "inherit"
    );
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

  //CART SECTION END

  //POPUP SECTION
  public redirectToProfilEdit() {
    this.router.navigateByUrl("/profile/edit");
    let message = "Please setup your profile first";
    let title = "Profile unfinished";
    let color = "black";
    this.extendedModalService.popup(
      this.customModalComponent,
      title,
      message,
      color
    );
  }

  //POPUP SECTION END

  //THEME SECTION
  public isDarkMode() {
    const theme = localStorage.getItem("Theme");
    return theme === "dark"
  }
  //THEME SECTION END

  handleEmptySpaces(str: string) {
    return str.replace(/\s+/g, '-');
  }

}
