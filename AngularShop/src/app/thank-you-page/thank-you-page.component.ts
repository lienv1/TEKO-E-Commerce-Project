import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';

@Component({
  selector: 'app-thank-you-page',
  templateUrl: './thank-you-page.component.html',
  styleUrls: ['./thank-you-page.component.scss']
})
export class ThankYouPageComponent implements OnInit {
  
  @ViewChild('mainContainer') mainContainer !: ElementRef<HTMLDivElement>
  
  ngOnInit(): void {
    if (this.mainContainer)
      this.scroll(this.mainContainer.nativeElement);
  }

  scroll(el: HTMLElement) {
    el.scrollIntoView({ behavior: 'smooth', block: 'start' });
  }

}
