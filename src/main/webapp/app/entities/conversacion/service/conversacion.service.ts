import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IConversacion, getConversacionIdentifier } from '../conversacion.model';

export type EntityResponseType = HttpResponse<IConversacion>;
export type EntityArrayResponseType = HttpResponse<IConversacion[]>;

@Injectable({ providedIn: 'root' })
export class ConversacionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/conversacions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(conversacion: IConversacion): Observable<EntityResponseType> {
    return this.http.post<IConversacion>(this.resourceUrl, conversacion, { observe: 'response' });
  }

  update(conversacion: IConversacion): Observable<EntityResponseType> {
    return this.http.put<IConversacion>(`${this.resourceUrl}/${getConversacionIdentifier(conversacion) as number}`, conversacion, {
      observe: 'response',
    });
  }

  partialUpdate(conversacion: IConversacion): Observable<EntityResponseType> {
    return this.http.patch<IConversacion>(`${this.resourceUrl}/${getConversacionIdentifier(conversacion) as number}`, conversacion, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IConversacion>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IConversacion[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addConversacionToCollectionIfMissing(
    conversacionCollection: IConversacion[],
    ...conversacionsToCheck: (IConversacion | null | undefined)[]
  ): IConversacion[] {
    const conversacions: IConversacion[] = conversacionsToCheck.filter(isPresent);
    if (conversacions.length > 0) {
      const conversacionCollectionIdentifiers = conversacionCollection.map(
        conversacionItem => getConversacionIdentifier(conversacionItem)!
      );
      const conversacionsToAdd = conversacions.filter(conversacionItem => {
        const conversacionIdentifier = getConversacionIdentifier(conversacionItem);
        if (conversacionIdentifier == null || conversacionCollectionIdentifiers.includes(conversacionIdentifier)) {
          return false;
        }
        conversacionCollectionIdentifiers.push(conversacionIdentifier);
        return true;
      });
      return [...conversacionsToAdd, ...conversacionCollection];
    }
    return conversacionCollection;
  }
}
