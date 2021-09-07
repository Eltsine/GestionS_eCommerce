import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MvtStkComponent } from '../list/mvt-stk.component';
import { MvtStkDetailComponent } from '../detail/mvt-stk-detail.component';
import { MvtStkUpdateComponent } from '../update/mvt-stk-update.component';
import { MvtStkRoutingResolveService } from './mvt-stk-routing-resolve.service';

const mvtStkRoute: Routes = [
  {
    path: '',
    component: MvtStkComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MvtStkDetailComponent,
    resolve: {
      mvtStk: MvtStkRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MvtStkUpdateComponent,
    resolve: {
      mvtStk: MvtStkRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MvtStkUpdateComponent,
    resolve: {
      mvtStk: MvtStkRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(mvtStkRoute)],
  exports: [RouterModule],
})
export class MvtStkRoutingModule {}
