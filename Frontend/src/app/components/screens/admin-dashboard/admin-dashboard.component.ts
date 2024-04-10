// import { Component, ComponentFactoryResolver, ComponentRef, ViewChild, ViewContainerRef } from '@angular/core';
// import { AddHotelFormComponent } from '../../forms/add-hotel-form/add-hotel-form.component';
// import { ContractFormComponent } from '../../forms/contract-form/contract-form.component';
// import { ModalComponent } from '../../popup/modal/modal.component';
// // import { UpdateHotelComponent } from '../update-hotel/update-hotel.component';
// // import { AddContractsComponent } from '../add-contracts/add-contracts.component';
// // import { ViewHotelComponent } from '../view-hotel/view-hotel.component';
// // import { ViewAllHotelsComponent } from '../view-all-hotels/view-all-hotels.component';

// @Component({
//   selector: 'app-admin-dashboard',
//   templateUrl: './admin-dashboard.component.html',
//   styleUrls: ['./admin-dashboard.component.css']
// })
// export class AdminDashboardComponent {
//   @ViewChild('dynamicComponentContainer', { read: ViewContainerRef }) dynamicComponentContainer!: ViewContainerRef;

//   // Declare component properties
//   addHotelComponent = AddHotelFormComponent;
//   addContractComponent = ContractFormComponent;
//   hotelEmailPopupComponent = ModalComponent;
//   // updateHotelComponent = UpdateHotelComponent;
//   // addContractsComponent = AddContractsComponent;
//   // viewHotelComponent = ViewHotelComponent;
//   // viewAllHotelsComponent = ViewAllHotelsComponent;

//   constructor(private componentFactoryResolver: ComponentFactoryResolver) {}

//   loadComponent(component: any) {
//     this.dynamicComponentContainer.clear();
//     const componentFactory = this.componentFactoryResolver.resolveComponentFactory(component);
//     this.dynamicComponentContainer.createComponent(componentFactory);
//     const componentRef: ComponentRef<any> = this.dynamicComponentContainer.createComponent(componentFactory);
//     if (component === this.addContractComponent) {
//       (componentRef.instance as ContractFormComponent).hotelId;
//     }
//   }

//   openHotelEmailPopup() {
//     const popupFactory = this.componentFactoryResolver.resolveComponentFactory(this.hotelEmailPopupComponent);
//     const popupRef = this.dynamicComponentContainer.createComponent(popupFactory);
//     popupRef.instance.hotelIdObtained.subscribe((hotelId: number) => {
//       this.loadComponent(this.contractFormComponent, hotelId);
//       popupRef.destroy(); // Close the popup after obtaining the hotelId
//     });
//   }
// }

// import { Component, ComponentFactoryResolver, ViewChild, ViewContainerRef } from '@angular/core';
// import { ModalComponent } from '../../popup/modal/modal.component';
// import { ContractFormComponent } from '../../forms/contract-form/contract-form.component';

// @Component({
//   selector: 'app-admin-dashboard',
//   templateUrl: './admin-dashboard.component.html',
//   styleUrls: ['./admin-dashboard.component.css']
// })
// export class AdminDashboardComponent {
//   @ViewChild('dynamicComponentContainer', { read: ViewContainerRef }) dynamicComponentContainer!: ViewContainerRef;

//   addHotelEmailModalComponent = ModalComponent;
//   contractFormComponent = ContractFormComponent;
//   hotelEmail: string | undefined;
//   hotelId!: number;

//   constructor(private componentFactoryResolver: ComponentFactoryResolver) {}

//   loadComponent(component: any) {
//     this.dynamicComponentContainer.clear();
//     const componentFactory = this.componentFactoryResolver.resolveComponentFactory(component);
//     const componentRef = this.dynamicComponentContainer.createComponent(componentFactory);
//     if (component === this.contractFormComponent) {
//       // Pass hotelId to the contract-form component
//       const componentInstance = componentRef.instance as AdminDashboardComponent;
//       componentInstance.hotelId = componentInstance.getHotelId(this.hotelEmail);
//     }
//   }

//   openAddHotelEmailModal() {
//     const modalFactory = this.componentFactoryResolver.resolveComponentFactory(this.addHotelEmailModalComponent);
//     const modalRef = this.dynamicComponentContainer.createComponent(modalFactory);
//     modalRef.instance.hotelEmailAdded.subscribe((email: string) => {
//       this.hotelEmail = email;
//       this.loadComponent(this.contractFormComponent);
//       modalRef.destroy(); // Close the modal after adding the hotel email
//     });
//   }

//   private getHotelId(email: string | undefined): number {
//     // Implement logic to get hotelId based on email
//     return 1; // For demonstration purpose
//   }
// }


import { Component, ComponentFactoryResolver, ComponentRef, ViewChild, ViewContainerRef } from '@angular/core';
import { AddHotelFormComponent } from '../../forms/add-hotel-form/add-hotel-form.component';
import { ContractFormComponent } from '../../forms/contract-form/contract-form.component';
import { ModalComponent } from '../../popup/modal/modal.component';
import { HotelService } from 'src/app/services/hotel.service'; // Import HotelService
import { Subscription } from 'rxjs'; // Import Subscription

@Component({
  selector: 'app-admin-dashboard',
  templateUrl: './admin-dashboard.component.html',
  styleUrls: ['./admin-dashboard.component.css']
})
export class AdminDashboardComponent {
  @ViewChild('dynamicComponentContainer', { read: ViewContainerRef }) dynamicComponentContainer!: ViewContainerRef;

  // Declare component properties
  addHotelComponent = AddHotelFormComponent;
  addContractComponent = ContractFormComponent;
  hotelEmailPopupComponent = ModalComponent;
  hotelIdSubscription!: Subscription; // Subscription to handle hotelId obtained from popup

  constructor(private componentFactoryResolver: ComponentFactoryResolver, private hotelService: HotelService) {}

  loadComponent(component: any, hotelId?: number) {
    this.dynamicComponentContainer.clear();
    const componentFactory = this.componentFactoryResolver.resolveComponentFactory(component);
    const componentRef: ComponentRef<any> = this.dynamicComponentContainer.createComponent(componentFactory);
    if (component === this.addContractComponent && hotelId) {
      (componentRef.instance as ContractFormComponent).hotelId = hotelId; // Set hotelId for ContractFormComponent
    }
  }

  openHotelEmailPopup() {
    const popupFactory = this.componentFactoryResolver.resolveComponentFactory(this.hotelEmailPopupComponent);
    const popupRef = this.dynamicComponentContainer.createComponent(popupFactory);
    popupRef.instance.hotelIdObtained.subscribe((hotelId: number) => {
      this.loadComponent(this.addContractComponent, hotelId);
      popupRef.destroy(); // Close the popup after obtaining the hotelId
    });
  }
  
}
