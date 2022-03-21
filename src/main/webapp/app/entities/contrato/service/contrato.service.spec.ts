import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IContrato, Contrato } from '../contrato.model';

import { ContratoService } from './contrato.service';

describe('Contrato Service', () => {
  let service: ContratoService;
  let httpMock: HttpTestingController;
  let elemDefault: IContrato;
  let expectedResult: IContrato | IContrato[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ContratoService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      preciofinal: 0,
      fecha: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          fecha: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Contrato', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          fecha: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fecha: currentDate,
        },
        returnedFromService
      );

      service.create(new Contrato()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Contrato', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          preciofinal: 1,
          fecha: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fecha: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Contrato', () => {
      const patchObject = Object.assign(
        {
          fecha: currentDate.format(DATE_TIME_FORMAT),
        },
        new Contrato()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          fecha: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Contrato', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          preciofinal: 1,
          fecha: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fecha: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Contrato', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addContratoToCollectionIfMissing', () => {
      it('should add a Contrato to an empty array', () => {
        const contrato: IContrato = { id: 123 };
        expectedResult = service.addContratoToCollectionIfMissing([], contrato);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(contrato);
      });

      it('should not add a Contrato to an array that contains it', () => {
        const contrato: IContrato = { id: 123 };
        const contratoCollection: IContrato[] = [
          {
            ...contrato,
          },
          { id: 456 },
        ];
        expectedResult = service.addContratoToCollectionIfMissing(contratoCollection, contrato);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Contrato to an array that doesn't contain it", () => {
        const contrato: IContrato = { id: 123 };
        const contratoCollection: IContrato[] = [{ id: 456 }];
        expectedResult = service.addContratoToCollectionIfMissing(contratoCollection, contrato);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(contrato);
      });

      it('should add only unique Contrato to an array', () => {
        const contratoArray: IContrato[] = [{ id: 123 }, { id: 456 }, { id: 86353 }];
        const contratoCollection: IContrato[] = [{ id: 123 }];
        expectedResult = service.addContratoToCollectionIfMissing(contratoCollection, ...contratoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const contrato: IContrato = { id: 123 };
        const contrato2: IContrato = { id: 456 };
        expectedResult = service.addContratoToCollectionIfMissing([], contrato, contrato2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(contrato);
        expect(expectedResult).toContain(contrato2);
      });

      it('should accept null and undefined values', () => {
        const contrato: IContrato = { id: 123 };
        expectedResult = service.addContratoToCollectionIfMissing([], null, contrato, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(contrato);
      });

      it('should return initial array if no Contrato is added', () => {
        const contratoCollection: IContrato[] = [{ id: 123 }];
        expectedResult = service.addContratoToCollectionIfMissing(contratoCollection, undefined, null);
        expect(expectedResult).toEqual(contratoCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
