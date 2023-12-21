import { NgbModal, NgbModalOptions } from "@ng-bootstrap/ng-bootstrap";
import { CustomModalComponent } from "../modal/custom-modal/custom-modal.component";
import { FunctionModel } from "../model/functionModel";

export class ExtendedModalService{

    constructor(private modalService:NgbModal){}

    public popup(modalComponent :CustomModalComponent, title: string, message: string, color: string, functionModals ?: FunctionModel[], autoclose ?:boolean, options ?: NgbModalOptions, waitMessage?:boolean) {
        modalComponent.message = message;
        modalComponent.title = title;
        modalComponent.colorTitle = color;
        modalComponent.waitMessage = waitMessage ? waitMessage : false;
        if (functionModals)
          modalComponent.functionModels = functionModals;
        else modalComponent.functionModels = [];
        this.openModal(modalComponent, autoclose, options);
      }

    private openModal(modal: any, autoclose ?: boolean, options ?: NgbModalOptions) {
        //let modalRef = this.modalService.open(modal.myModal);
        let modalRef = options ? this.modalService.open(modal.myModal,options) : this.modalService.open(modal.myModal);  
        if (autoclose) {
          setTimeout(() => {
            modalRef.dismiss();
          }, 3000);
        }
      }

    public closeAllModal(){
        this.modalService.dismissAll();
    }

}