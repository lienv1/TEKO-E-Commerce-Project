import { Component, OnInit, ViewChild } from '@angular/core';
import { FunctionModel } from 'src/app/model/functionModel';

@Component({
  selector: 'app-custom-modal',
  templateUrl: './custom-modal.component.html',
  styleUrls: ['./custom-modal.component.scss']
})
export class CustomModalComponent implements OnInit {
  
  public functionModels : FunctionModel[] = [];
  public title !: string;
  public message !: string;
  public colorTitle :string = "black";
  public waitMessage : boolean = false;

  constructor() { }

  @ViewChild('myModal') myModal: any;

  ngOnInit(): void {
  }

}
