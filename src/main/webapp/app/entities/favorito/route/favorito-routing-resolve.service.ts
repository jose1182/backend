import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFavorito, Favorito } from '../favorito.model';
import { FavoritoService } from '../service/favorito.service';

@Injectable({ providedIn: 'root' })
export class FavoritoRoutingResolveService implements Resolve<IFavorito> {
  constructor(protected service: FavoritoService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFavorito> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((favorito: HttpResponse<Favorito>) => {
          if (favorito.body) {
            return of(favorito.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Favorito());
  }
}
