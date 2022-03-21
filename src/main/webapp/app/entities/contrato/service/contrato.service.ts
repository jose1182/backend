import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IContrato, getContratoIdentifier } from '../contrato.model';

export type EntityResponseType = HttpResponse<IContrato>;
export type EntityArrayResponseType = HttpResponse<IContrato[]>;

@Injectable({ providedIn: 'root' })
export class ContratoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/contratoes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(contrato: IContrato): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(contrato);
    return this.http
      .post<IContrato>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(contrato: IContrato): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(contrato);
    return this.http
      .put<IContrato>(`${this.resourceUrl}/${getContratoIdentifier(contrato) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(contrato: IContrato): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(contrato);
    return this.http
      .patch<IContrato>(`${this.resourceUrl}/${getContratoIdentifier(contrato) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IContrato>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IContrato[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addContratoToCollectionIfMissing(contratoCollection: IContrato[], ...contratoesToCheck: (IContrato | null | undefined)[]): IContrato[] {
    const contratoes: IContrato[] = contratoesToCheck.filter(isPresent);
    if (contratoes.length > 0) {
      const contratoCollectionIdentifiers = contratoCollection.map(contratoItem => getContratoIdentifier(contratoItem)!);
      const contratoesToAdd = contratoes.filter(contratoItem => {
        const contratoIdentifier = getContratoIdentifier(contratoItem);
        if (contratoIdentifier == null || contratoCollectionIdentifiers.includes(contratoIdentifier)) {
          return false;
        }
        contratoCollectionIdentifiers.push(contratoIdentifier);
        return true;
      });
      return [...contratoesToAdd, ...contratoCollection];
    }
    return contratoCollection;
  }

  protected convertDateFromClient(contrato: IContrato): IContrato {
    return Object.assign({}, contrato, {
      fecha: contrato.fecha?.isValid() ? contrato.fecha.toJSON() : undefined,
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
      res.body.forEach((contrato: IContrato) => {
        contrato.fecha = contrato.fecha ? dayjs(contrato.fecha) : undefined;
      });
    }
    return res;
  }
}
