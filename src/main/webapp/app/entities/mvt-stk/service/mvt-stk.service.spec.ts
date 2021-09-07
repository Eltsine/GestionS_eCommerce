import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { TypeMvtStk } from 'app/entities/enumerations/type-mvt-stk.model';
import { IMvtStk, MvtStk } from '../mvt-stk.model';

import { MvtStkService } from './mvt-stk.service';

describe('Service Tests', () => {
  describe('MvtStk Service', () => {
    let service: MvtStkService;
    let httpMock: HttpTestingController;
    let elemDefault: IMvtStk;
    let expectedResult: IMvtStk | IMvtStk[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(MvtStkService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        dateMvt: currentDate,
        quantite: 0,
        typeMvtStk: TypeMvtStk.ENTREE,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            dateMvt: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a MvtStk', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            dateMvt: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateMvt: currentDate,
          },
          returnedFromService
        );

        service.create(new MvtStk()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a MvtStk', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            dateMvt: currentDate.format(DATE_TIME_FORMAT),
            quantite: 1,
            typeMvtStk: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateMvt: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a MvtStk', () => {
        const patchObject = Object.assign(
          {
            typeMvtStk: 'BBBBBB',
          },
          new MvtStk()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            dateMvt: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of MvtStk', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            dateMvt: currentDate.format(DATE_TIME_FORMAT),
            quantite: 1,
            typeMvtStk: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateMvt: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a MvtStk', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addMvtStkToCollectionIfMissing', () => {
        it('should add a MvtStk to an empty array', () => {
          const mvtStk: IMvtStk = { id: 123 };
          expectedResult = service.addMvtStkToCollectionIfMissing([], mvtStk);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(mvtStk);
        });

        it('should not add a MvtStk to an array that contains it', () => {
          const mvtStk: IMvtStk = { id: 123 };
          const mvtStkCollection: IMvtStk[] = [
            {
              ...mvtStk,
            },
            { id: 456 },
          ];
          expectedResult = service.addMvtStkToCollectionIfMissing(mvtStkCollection, mvtStk);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a MvtStk to an array that doesn't contain it", () => {
          const mvtStk: IMvtStk = { id: 123 };
          const mvtStkCollection: IMvtStk[] = [{ id: 456 }];
          expectedResult = service.addMvtStkToCollectionIfMissing(mvtStkCollection, mvtStk);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(mvtStk);
        });

        it('should add only unique MvtStk to an array', () => {
          const mvtStkArray: IMvtStk[] = [{ id: 123 }, { id: 456 }, { id: 72234 }];
          const mvtStkCollection: IMvtStk[] = [{ id: 123 }];
          expectedResult = service.addMvtStkToCollectionIfMissing(mvtStkCollection, ...mvtStkArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const mvtStk: IMvtStk = { id: 123 };
          const mvtStk2: IMvtStk = { id: 456 };
          expectedResult = service.addMvtStkToCollectionIfMissing([], mvtStk, mvtStk2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(mvtStk);
          expect(expectedResult).toContain(mvtStk2);
        });

        it('should accept null and undefined values', () => {
          const mvtStk: IMvtStk = { id: 123 };
          expectedResult = service.addMvtStkToCollectionIfMissing([], null, mvtStk, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(mvtStk);
        });

        it('should return initial array if no MvtStk is added', () => {
          const mvtStkCollection: IMvtStk[] = [{ id: 123 }];
          expectedResult = service.addMvtStkToCollectionIfMissing(mvtStkCollection, undefined, null);
          expect(expectedResult).toEqual(mvtStkCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
