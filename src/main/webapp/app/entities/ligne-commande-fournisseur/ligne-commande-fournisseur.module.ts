import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { LigneCommandeFournisseurComponent } from './list/ligne-commande-fournisseur.component';
import { LigneCommandeFournisseurDetailComponent } from './detail/ligne-commande-fournisseur-detail.component';
import { LigneCommandeFournisseurUpdateComponent } from './update/ligne-commande-fournisseur-update.component';
import { LigneCommandeFournisseurDeleteDialogComponent } from './delete/ligne-commande-fournisseur-delete-dialog.component';
import { LigneCommandeFournisseurRoutingModule } from './route/ligne-commande-fournisseur-routing.module';

@NgModule({
  imports: [SharedModule, LigneCommandeFournisseurRoutingModule],
  declarations: [
    LigneCommandeFournisseurComponent,
    LigneCommandeFournisseurDetailComponent,
    LigneCommandeFournisseurUpdateComponent,
    LigneCommandeFournisseurDeleteDialogComponent,
  ],
  entryComponents: [LigneCommandeFournisseurDeleteDialogComponent],
})
export class LigneCommandeFournisseurModule {}
