import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMensaje, getMensajeIdentifier } from '../mensaje.model';

export type EntityResponseType = HttpResponse<IMensaje>;
export type EntityArrayResponseType = HttpResponse<IMensaje[]>;

@Injectable({ providedIn: 'root' })
export class MensajeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/mensajes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(mensaje: IMensaje): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(mensaje);
    return this.http
      .post<IMensaje>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(mensaje: IMensaje): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(mensaje);
    return this.http
      .put<IMensaje>(`${this.resourceUrl}/${getMensajeIdentifier(mensaje) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(mensaje: IMensaje): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(mensaje);
    return this.http
      .patch<IMensaje>(`${this.resourceUrl}/${getMensajeIdentifier(mensaje) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IMensaje>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IMensaje[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addMensajeToCollectionIfMissing(mensajeCollection: IMensaje[], ...mensajesToCheck: (IMensaje | null | undefined)[]): IMensaje[] {
    const mensajes: IMensaje[] = mensajesToCheck.filter(isPresent);
    if (mensajes.length > 0) {
      const mensajeCollectionIdentifiers = mensajeCollection.map(mensajeItem => getMensajeIdentifier(mensajeItem)!);
      const mensajesToAdd = mensajes.filter(mensajeItem => {
        const mensajeIdentifier = getMensajeIdentifier(mensajeItem);
        if (mensajeIdentifier == null || mensajeCollectionIdentifiers.includes(mensajeIdentifier)) {
          return false;
        }
        mensajeCollectionIdentifiers.push(mensajeIdentifier);
        return true;
      });
      return [...mensajesToAdd, ...mensajeCollection];
    }
    return mensajeCollection;
  }

  protected convertDateFromClient(mensaje: IMensaje): IMensaje {
    return Object.assign({}, mensaje, {
      fecha: mensaje.fecha?.isValid() ? mensaje.fecha.toJSON() : undefined,
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
      res.body.forEach((mensaje: IMensaje) => {
        mensaje.fecha = mensaje.fecha ? dayjs(mensaje.fecha) : undefined;
      });
    }
    return res;
  }
}
