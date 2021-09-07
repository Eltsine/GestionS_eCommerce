import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { MvtStkComponent } from './list/mvt-stk.component';
import { MvtStkDetailComponent } from './detail/mvt-stk-detail.component';
import { MvtStkUpdateComponent } from './update/mvt-stk-update.component';
import { MvtStkDeleteDialogComponent } from './delete/mvt-stk-delete-dialog.component';
import { MvtStkRoutingModule } from './route/mvt-stk-routing.module';

@NgModule({
  imports: [SharedModule, MvtStkRoutingModule],
  declarations: [MvtStkComponent, MvtStkDetailComponent, MvtStkUpdateComponent, MvtStkDeleteDialogComponent],
  entryComponents: [MvtStkDeleteDialogComponent],
})
export class MvtStkModule {}
