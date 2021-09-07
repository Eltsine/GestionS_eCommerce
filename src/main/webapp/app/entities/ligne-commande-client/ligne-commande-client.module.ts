import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { LigneCommandeClientComponent } from './list/ligne-commande-client.component';
import { LigneCommandeClientDetailComponent } from './detail/ligne-commande-client-detail.component';
import { LigneCommandeClientUpdateComponent } from './update/ligne-commande-client-update.component';
import { LigneCommandeClientDeleteDialogComponent } from './delete/ligne-commande-client-delete-dialog.component';
import { LigneCommandeClientRoutingModule } from './route/ligne-commande-client-routing.module';

@NgModule({
  imports: [SharedModule, LigneCommandeClientRoutingModule],
  declarations: [
    LigneCommandeClientComponent,
    LigneCommandeClientDetailComponent,
    LigneCommandeClientUpdateComponent,
    LigneCommandeClientDeleteDialogComponent,
  ],
  entryComponents: [LigneCommandeClientDeleteDialogComponent],
})
export class LigneCommandeClientModule {}
