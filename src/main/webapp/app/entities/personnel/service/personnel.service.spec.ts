import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IPersonnel, Personnel } from '../personnel.model';

import { PersonnelService } from './personnel.service';

describe('Service Tests', () => {
  describe('Personnel Service', () => {
    let service: PersonnelService;
    let httpMock: HttpTestingController;
    let elemDefault: IPersonnel;
    let expectedResult: IPersonnel | IPersonnel[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(PersonnelService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        nom: 'AAAAAAA',
        prenom: 'AAAAAAA',
        dateNaissance: currentDate,
        email: 'AAAAAAA',
        motDePass: 'AAAAAAA',
        photoContentType: 'image/png',
        photo: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            dateNaissance: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Personnel', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            dateNaissance: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateNaissance: currentDate,
          },
          returnedFromService
        );

        service.create(new Personnel()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Personnel', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nom: 'BBBBBB',
            prenom: 'BBBBBB',
            dateNaissance: currentDate.format(DATE_TIME_FORMAT),
            email: 'BBBBBB',
            motDePass: 'BBBBBB',
            photo: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateNaissance: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Personnel', () => {
        const patchObject = Object.assign(
          {
            prenom: 'BBBBBB',
            email: 'BBBBBB',
            photo: 'BBBBBB',
          },
          new Personnel()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            dateNaissance: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Personnel', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nom: 'BBBBBB',
            prenom: 'BBBBBB',
            dateNaissance: currentDate.format(DATE_TIME_FORMAT),
            email: 'BBBBBB',
            motDePass: 'BBBBBB',
            photo: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateNaissance: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Personnel', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addPersonnelToCollectionIfMissing', () => {
        it('should add a Personnel to an empty array', () => {
          const personnel: IPersonnel = { id: 123 };
          expectedResult = service.addPersonnelToCollectionIfMissing([], personnel);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(personnel);
        });

        it('should not add a Personnel to an array that contains it', () => {
          const personnel: IPersonnel = { id: 123 };
          const personnelCollection: IPersonnel[] = [
            {
              ...personnel,
            },
            { id: 456 },
          ];
          expectedResult = service.addPersonnelToCollectionIfMissing(personnelCollection, personnel);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Personnel to an array that doesn't contain it", () => {
          const personnel: IPersonnel = { id: 123 };
          const personnelCollection: IPersonnel[] = [{ id: 456 }];
          expectedResult = service.addPersonnelToCollectionIfMissing(personnelCollection, personnel);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(personnel);
        });

        it('should add only unique Personnel to an array', () => {
          const personnelArray: IPersonnel[] = [{ id: 123 }, { id: 456 }, { id: 4666 }];
          const personnelCollection: IPersonnel[] = [{ id: 123 }];
          expectedResult = service.addPersonnelToCollectionIfMissing(personnelCollection, ...personnelArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const personnel: IPersonnel = { id: 123 };
          const personnel2: IPersonnel = { id: 456 };
          expectedResult = service.addPersonnelToCollectionIfMissing([], personnel, personnel2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(personnel);
          expect(expectedResult).toContain(personnel2);
        });

        it('should accept null and undefined values', () => {
          const personnel: IPersonnel = { id: 123 };
          expectedResult = service.addPersonnelToCollectionIfMissing([], null, personnel, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(personnel);
        });

        it('should return initial array if no Personnel is added', () => {
          const personnelCollection: IPersonnel[] = [{ id: 123 }];
          expectedResult = service.addPersonnelToCollectionIfMissing(personnelCollection, undefined, null);
          expect(expectedResult).toEqual(personnelCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
