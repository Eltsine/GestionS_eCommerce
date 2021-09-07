import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { LigneVenteComponent } from './list/ligne-vente.component';
import { LigneVenteDetailComponent } from './detail/ligne-vente-detail.component';
import { LigneVenteUpdateComponent } from './update/ligne-vente-update.component';
import { LigneVenteDeleteDialogComponent } from './delete/ligne-vente-delete-dialog.component';
import { LigneVenteRoutingModule } from './route/ligne-vente-routing.module';

@NgModule({
  imports: [SharedModule, LigneVenteRoutingModule],
  declarations: [LigneVenteComponent, LigneVenteDetailComponent, LigneVenteUpdateComponent, LigneVenteDeleteDialogComponent],
  entryComponents: [LigneVenteDeleteDialogComponent],
})
export class LigneVenteModule {}
