import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IServicio, getServicioIdentifier } from '../servicio.model';

export type EntityResponseType = HttpResponse<IServicio>;
export type EntityArrayResponseType = HttpResponse<IServicio[]>;

@Injectable({ providedIn: 'root' })
export class ServicioService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/servicios');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(servicio: IServicio): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(servicio);
    return this.http
      .post<IServicio>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(servicio: IServicio): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(servicio);
    return this.http
      .put<IServicio>(`${this.resourceUrl}/${getServicioIdentifier(servicio) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(servicio: IServicio): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(servicio);
    return this.http
      .patch<IServicio>(`${this.resourceUrl}/${getServicioIdentifier(servicio) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IServicio>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IServicio[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addServicioToCollectionIfMissing(servicioCollection: IServicio[], ...serviciosToCheck: (IServicio | null | undefined)[]): IServicio[] {
    const servicios: IServicio[] = serviciosToCheck.filter(isPresent);
    if (servicios.length > 0) {
      const servicioCollectionIdentifiers = servicioCollection.map(servicioItem => getServicioIdentifier(servicioItem)!);
      const serviciosToAdd = servicios.filter(servicioItem => {
        const servicioIdentifier = getServicioIdentifier(servicioItem);
        if (servicioIdentifier == null || servicioCollectionIdentifiers.includes(servicioIdentifier)) {
          return false;
        }
        servicioCollectionIdentifiers.push(servicioIdentifier);
        return true;
      });
      return [...serviciosToAdd, ...servicioCollection];
    }
    return servicioCollection;
  }

  protected convertDateFromClient(servicio: IServicio): IServicio {
    return Object.assign({}, servicio, {
      fechacreacion: servicio.fechacreacion?.isValid() ? servicio.fechacreacion.toJSON() : undefined,
      fechaactualizacion: servicio.fechaactualizacion?.isValid() ? servicio.fechaactualizacion.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.fechacreacion = res.body.fechacreacion ? dayjs(res.body.fechacreacion) : undefined;
      res.body.fechaactualizacion = res.body.fechaactualizacion ? dayjs(res.body.fechaactualizacion) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((servicio: IServicio) => {
        servicio.fechacreacion = servicio.fechacreacion ? dayjs(servicio.fechacreacion) : undefined;
        servicio.fechaactualizacion = servicio.fechaactualizacion ? dayjs(servicio.fechaactualizacion) : undefined;
      });
    }
    return res;
  }
}
