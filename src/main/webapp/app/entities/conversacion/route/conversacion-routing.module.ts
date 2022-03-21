import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ConversacionComponent } from '../list/conversacion.component';
import { ConversacionDetailComponent } from '../detail/conversacion-detail.component';
import { ConversacionUpdateComponent } from '../update/conversacion-update.component';
import { ConversacionRoutingResolveService } from './conversacion-routing-resolve.service';

const conversacionRoute: Routes = [
  {
    path: '',
    component: ConversacionComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ConversacionDetailComponent,
    resolve: {
      conversacion: ConversacionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ConversacionUpdateComponent,
    resolve: {
      conversacion: ConversacionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ConversacionUpdateComponent,
    resolve: {
      conversacion: ConversacionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(conversacionRoute)],
  exports: [RouterModule],
})
export class ConversacionRoutingModule {}
