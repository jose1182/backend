import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IConversacion, Conversacion } from '../conversacion.model';

import { ConversacionService } from './conversacion.service';

describe('Conversacion Service', () => {
  let service: ConversacionService;
  let httpMock: HttpTestingController;
  let elemDefault: IConversacion;
  let expectedResult: IConversacion | IConversacion[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ConversacionService);
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

    it('should create a Conversacion', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Conversacion()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Conversacion', () => {
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

    it('should partial update a Conversacion', () => {
      const patchObject = Object.assign({}, new Conversacion());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Conversacion', () => {
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

    it('should delete a Conversacion', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addConversacionToCollectionIfMissing', () => {
      it('should add a Conversacion to an empty array', () => {
        const conversacion: IConversacion = { id: 123 };
        expectedResult = service.addConversacionToCollectionIfMissing([], conversacion);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(conversacion);
      });

      it('should not add a Conversacion to an array that contains it', () => {
        const conversacion: IConversacion = { id: 123 };
        const conversacionCollection: IConversacion[] = [
          {
            ...conversacion,
          },
          { id: 456 },
        ];
        expectedResult = service.addConversacionToCollectionIfMissing(conversacionCollection, conversacion);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Conversacion to an array that doesn't contain it", () => {
        const conversacion: IConversacion = { id: 123 };
        const conversacionCollection: IConversacion[] = [{ id: 456 }];
        expectedResult = service.addConversacionToCollectionIfMissing(conversacionCollection, conversacion);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(conversacion);
      });

      it('should add only unique Conversacion to an array', () => {
        const conversacionArray: IConversacion[] = [{ id: 123 }, { id: 456 }, { id: 85518 }];
        const conversacionCollection: IConversacion[] = [{ id: 123 }];
        expectedResult = service.addConversacionToCollectionIfMissing(conversacionCollection, ...conversacionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const conversacion: IConversacion = { id: 123 };
        const conversacion2: IConversacion = { id: 456 };
        expectedResult = service.addConversacionToCollectionIfMissing([], conversacion, conversacion2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(conversacion);
        expect(expectedResult).toContain(conversacion2);
      });

      it('should accept null and undefined values', () => {
        const conversacion: IConversacion = { id: 123 };
        expectedResult = service.addConversacionToCollectionIfMissing([], null, conversacion, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(conversacion);
      });

      it('should return initial array if no Conversacion is added', () => {
        const conversacionCollection: IConversacion[] = [{ id: 123 }];
        expectedResult = service.addConversacionToCollectionIfMissing(conversacionCollection, undefined, null);
        expect(expectedResult).toEqual(conversacionCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
