import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFavorito, getFavoritoIdentifier } from '../favorito.model';

export type EntityResponseType = HttpResponse<IFavorito>;
export type EntityArrayResponseType = HttpResponse<IFavorito[]>;

@Injectable({ providedIn: 'root' })
export class FavoritoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/favoritos');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(favorito: IFavorito): Observable<EntityResponseType> {
    return this.http.post<IFavorito>(this.resourceUrl, favorito, { observe: 'response' });
  }

  update(favorito: IFavorito): Observable<EntityResponseType> {
    return this.http.put<IFavorito>(`${this.resourceUrl}/${getFavoritoIdentifier(favorito) as number}`, favorito, { observe: 'response' });
  }

  partialUpdate(favorito: IFavorito): Observable<EntityResponseType> {
    return this.http.patch<IFavorito>(`${this.resourceUrl}/${getFavoritoIdentifier(favorito) as number}`, favorito, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFavorito>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFavorito[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addFavoritoToCollectionIfMissing(favoritoCollection: IFavorito[], ...favoritosToCheck: (IFavorito | null | undefined)[]): IFavorito[] {
    const favoritos: IFavorito[] = favoritosToCheck.filter(isPresent);
    if (favoritos.length > 0) {
      const favoritoCollectionIdentifiers = favoritoCollection.map(favoritoItem => getFavoritoIdentifier(favoritoItem)!);
      const favoritosToAdd = favoritos.filter(favoritoItem => {
        const favoritoIdentifier = getFavoritoIdentifier(favoritoItem);
        if (favoritoIdentifier == null || favoritoCollectionIdentifiers.includes(favoritoIdentifier)) {
          return false;
        }
        favoritoCollectionIdentifiers.push(favoritoIdentifier);
        return true;
      });
      return [...favoritosToAdd, ...favoritoCollection];
    }
    return favoritoCollection;
  }
}
