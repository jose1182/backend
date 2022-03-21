import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IContrato, Contrato } from '../contrato.model';
import { ContratoService } from '../service/contrato.service';

@Injectable({ providedIn: 'root' })
export class ContratoRoutingResolveService implements Resolve<IContrato> {
  constructor(protected service: ContratoService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IContrato> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((contrato: HttpResponse<Contrato>) => {
          if (contrato.body) {
            return of(contrato.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Contrato());
  }
}
