import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CommandeFournisseurComponent } from './list/commande-fournisseur.component';
import { CommandeFournisseurDetailComponent } from './detail/commande-fournisseur-detail.component';
import { CommandeFournisseurUpdateComponent } from './update/commande-fournisseur-update.component';
import { CommandeFournisseurDeleteDialogComponent } from './delete/commande-fournisseur-delete-dialog.component';
import { CommandeFournisseurRoutingModule } from './route/commande-fournisseur-routing.module';

@NgModule({
  imports: [SharedModule, CommandeFournisseurRoutingModule],
  declarations: [
    CommandeFournisseurComponent,
    CommandeFournisseurDetailComponent,
    CommandeFournisseurUpdateComponent,
    CommandeFournisseurDeleteDialogComponent,
  ],
  entryComponents: [CommandeFournisseurDeleteDialogComponent],
})
export class CommandeFournisseurModule {}
