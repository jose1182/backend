import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ValoracionComponent } from '../list/valoracion.component';
import { ValoracionDetailComponent } from '../detail/valoracion-detail.component';
import { ValoracionUpdateComponent } from '../update/valoracion-update.component';
import { ValoracionRoutingResolveService } from './valoracion-routing-resolve.service';

const valoracionRoute: Routes = [
  {
    path: '',
    component: ValoracionComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ValoracionDetailComponent,
    resolve: {
      valoracion: ValoracionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ValoracionUpdateComponent,
    resolve: {
      valoracion: ValoracionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ValoracionUpdateComponent,
    resolve: {
      valoracion: ValoracionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(valoracionRoute)],
  exports: [RouterModule],
})
export class ValoracionRoutingModule {}
