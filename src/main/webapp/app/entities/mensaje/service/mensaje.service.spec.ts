import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IMensaje, Mensaje } from '../mensaje.model';

import { MensajeService } from './mensaje.service';

describe('Mensaje Service', () => {
  let service: MensajeService;
  let httpMock: HttpTestingController;
  let elemDefault: IMensaje;
  let expectedResult: IMensaje | IMensaje[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(MensajeService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      texto: 'AAAAAAA',
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

    it('should create a Mensaje', () => {
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

      service.create(new Mensaje()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Mensaje', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          texto: 'BBBBBB',
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

    it('should partial update a Mensaje', () => {
      const patchObject = Object.assign({}, new Mensaje());

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

    it('should return a list of Mensaje', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          texto: 'BBBBBB',
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

    it('should delete a Mensaje', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addMensajeToCollectionIfMissing', () => {
      it('should add a Mensaje to an empty array', () => {
        const mensaje: IMensaje = { id: 123 };
        expectedResult = service.addMensajeToCollectionIfMissing([], mensaje);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(mensaje);
      });

      it('should not add a Mensaje to an array that contains it', () => {
        const mensaje: IMensaje = { id: 123 };
        const mensajeCollection: IMensaje[] = [
          {
            ...mensaje,
          },
          { id: 456 },
        ];
        expectedResult = service.addMensajeToCollectionIfMissing(mensajeCollection, mensaje);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Mensaje to an array that doesn't contain it", () => {
        const mensaje: IMensaje = { id: 123 };
        const mensajeCollection: IMensaje[] = [{ id: 456 }];
        expectedResult = service.addMensajeToCollectionIfMissing(mensajeCollection, mensaje);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(mensaje);
      });

      it('should add only unique Mensaje to an array', () => {
        const mensajeArray: IMensaje[] = [{ id: 123 }, { id: 456 }, { id: 57644 }];
        const mensajeCollection: IMensaje[] = [{ id: 123 }];
        expectedResult = service.addMensajeToCollectionIfMissing(mensajeCollection, ...mensajeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const mensaje: IMensaje = { id: 123 };
        const mensaje2: IMensaje = { id: 456 };
        expectedResult = service.addMensajeToCollectionIfMissing([], mensaje, mensaje2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(mensaje);
        expect(expectedResult).toContain(mensaje2);
      });

      it('should accept null and undefined values', () => {
        const mensaje: IMensaje = { id: 123 };
        expectedResult = service.addMensajeToCollectionIfMissing([], null, mensaje, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(mensaje);
      });

      it('should return initial array if no Mensaje is added', () => {
        const mensajeCollection: IMensaje[] = [{ id: 123 }];
        expectedResult = service.addMensajeToCollectionIfMissing(mensajeCollection, undefined, null);
        expect(expectedResult).toEqual(mensajeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
