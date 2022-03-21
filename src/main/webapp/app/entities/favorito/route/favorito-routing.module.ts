import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FavoritoComponent } from '../list/favorito.component';
import { FavoritoDetailComponent } from '../detail/favorito-detail.component';
import { FavoritoUpdateComponent } from '../update/favorito-update.component';
import { FavoritoRoutingResolveService } from './favorito-routing-resolve.service';

const favoritoRoute: Routes = [
  {
    path: '',
    component: FavoritoComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FavoritoDetailComponent,
    resolve: {
      favorito: FavoritoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FavoritoUpdateComponent,
    resolve: {
      favorito: FavoritoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FavoritoUpdateComponent,
    resolve: {
      favorito: FavoritoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(favoritoRoute)],
  exports: [RouterModule],
})
export class FavoritoRoutingModule {}
