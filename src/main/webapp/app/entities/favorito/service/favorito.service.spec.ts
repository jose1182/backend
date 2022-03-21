import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IFavorito, Favorito } from '../favorito.model';

import { FavoritoService } from './favorito.service';

describe('Favorito Service', () => {
  let service: FavoritoService;
  let httpMock: HttpTestingController;
  let elemDefault: IFavorito;
  let expectedResult: IFavorito | IFavorito[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(FavoritoService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Favorito', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Favorito()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Favorito', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Favorito', () => {
      const patchObject = Object.assign({}, new Favorito());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Favorito', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Favorito', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addFavoritoToCollectionIfMissing', () => {
      it('should add a Favorito to an empty array', () => {
        const favorito: IFavorito = { id: 123 };
        expectedResult = service.addFavoritoToCollectionIfMissing([], favorito);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(favorito);
      });

      it('should not add a Favorito to an array that contains it', () => {
        const favorito: IFavorito = { id: 123 };
        const favoritoCollection: IFavorito[] = [
          {
            ...favorito,
          },
          { id: 456 },
        ];
        expectedResult = service.addFavoritoToCollectionIfMissing(favoritoCollection, favorito);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Favorito to an array that doesn't contain it", () => {
        const favorito: IFavorito = { id: 123 };
        const favoritoCollection: IFavorito[] = [{ id: 456 }];
        expectedResult = service.addFavoritoToCollectionIfMissing(favoritoCollection, favorito);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(favorito);
      });

      it('should add only unique Favorito to an array', () => {
        const favoritoArray: IFavorito[] = [{ id: 123 }, { id: 456 }, { id: 45941 }];
        const favoritoCollection: IFavorito[] = [{ id: 123 }];
        expectedResult = service.addFavoritoToCollectionIfMissing(favoritoCollection, ...favoritoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const favorito: IFavorito = { id: 123 };
        const favorito2: IFavorito = { id: 456 };
        expectedResult = service.addFavoritoToCollectionIfMissing([], favorito, favorito2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(favorito);
        expect(expectedResult).toContain(favorito2);
      });

      it('should accept null and undefined values', () => {
        const favorito: IFavorito = { id: 123 };
        expectedResult = service.addFavoritoToCollectionIfMissing([], null, favorito, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(favorito);
      });

      it('should return initial array if no Favorito is added', () => {
        const favoritoCollection: IFavorito[] = [{ id: 123 }];
        expectedResult = service.addFavoritoToCollectionIfMissing(favoritoCollection, undefined, null);
        expect(expectedResult).toEqual(favoritoCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
