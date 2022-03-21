import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MensajeComponent } from '../list/mensaje.component';
import { MensajeDetailComponent } from '../detail/mensaje-detail.component';
import { MensajeUpdateComponent } from '../update/mensaje-update.component';
import { MensajeRoutingResolveService } from './mensaje-routing-resolve.service';

const mensajeRoute: Routes = [
  {
    path: '',
    component: MensajeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MensajeDetailComponent,
    resolve: {
      mensaje: MensajeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MensajeUpdateComponent,
    resolve: {
      mensaje: MensajeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MensajeUpdateComponent,
    resolve: {
      mensaje: MensajeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(mensajeRoute)],
  exports: [RouterModule],
})
export class MensajeRoutingModule {}
