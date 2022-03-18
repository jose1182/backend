import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IValoracion, getValoracionIdentifier } from '../valoracion.model';

export type EntityResponseType = HttpResponse<IValoracion>;
export type EntityArrayResponseType = HttpResponse<IValoracion[]>;

@Injectable({ providedIn: 'root' })
export class ValoracionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/valoracions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(valoracion: IValoracion): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(valoracion);
    return this.http
      .post<IValoracion>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(valoracion: IValoracion): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(valoracion);
    return this.http
      .put<IValoracion>(`${this.resourceUrl}/${getValoracionIdentifier(valoracion) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(valoracion: IValoracion): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(valoracion);
    return this.http
      .patch<IValoracion>(`${this.resourceUrl}/${getValoracionIdentifier(valoracion) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IValoracion>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IValoracion[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addValoracionToCollectionIfMissing(
    valoracionCollection: IValoracion[],
    ...valoracionsToCheck: (IValoracion | null | undefined)[]
  ): IValoracion[] {
    const valoracions: IValoracion[] = valoracionsToCheck.filter(isPresent);
    if (valoracions.length > 0) {
      const valoracionCollectionIdentifiers = valoracionCollection.map(valoracionItem => getValoracionIdentifier(valoracionItem)!);
      const valoracionsToAdd = valoracions.filter(valoracionItem => {
        const valoracionIdentifier = getValoracionIdentifier(valoracionItem);
        if (valoracionIdentifier == null || valoracionCollectionIdentifiers.includes(valoracionIdentifier)) {
          return false;
        }
        valoracionCollectionIdentifiers.push(valoracionIdentifier);
        return true;
      });
      return [...valoracionsToAdd, ...valoracionCollection];
    }
    return valoracionCollection;
  }

  protected convertDateFromClient(valoracion: IValoracion): IValoracion {
    return Object.assign({}, valoracion, {
      fecha: valoracion.fecha?.isValid() ? valoracion.fecha.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.fecha = res.body.fecha ? dayjs(res.body.fecha) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((valoracion: IValoracion) => {
        valoracion.fecha = valoracion.fecha ? dayjs(valoracion.fecha) : undefined;
      });
    }
    return res;
  }
}
