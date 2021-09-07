import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ILigneCommandeClient, LigneCommandeClient } from '../ligne-commande-client.model';

import { LigneCommandeClientService } from './ligne-commande-client.service';

describe('Service Tests', () => {
  describe('LigneCommandeClient Service', () => {
    let service: LigneCommandeClientService;
    let httpMock: HttpTestingController;
    let elemDefault: ILigneCommandeClient;
    let expectedResult: ILigneCommandeClient | ILigneCommandeClient[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(LigneCommandeClientService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        quantite: 0,
        prixUnitaire: 0,
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

      it('should create a LigneCommandeClient', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new LigneCommandeClient()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a LigneCommandeClient', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            quantite: 1,
            prixUnitaire: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a LigneCommandeClient', () => {
        const patchObject = Object.assign({}, new LigneCommandeClient());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of LigneCommandeClient', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            quantite: 1,
            prixUnitaire: 1,
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

      it('should delete a LigneCommandeClient', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addLigneCommandeClientToCollectionIfMissing', () => {
        it('should add a LigneCommandeClient to an empty array', () => {
          const ligneCommandeClient: ILigneCommandeClient = { id: 123 };
          expectedResult = service.addLigneCommandeClientToCollectionIfMissing([], ligneCommandeClient);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(ligneCommandeClient);
        });

        it('should not add a LigneCommandeClient to an array that contains it', () => {
          const ligneCommandeClient: ILigneCommandeClient = { id: 123 };
          const ligneCommandeClientCollection: ILigneCommandeClient[] = [
            {
              ...ligneCommandeClient,
            },
            { id: 456 },
          ];
          expectedResult = service.addLigneCommandeClientToCollectionIfMissing(ligneCommandeClientCollection, ligneCommandeClient);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a LigneCommandeClient to an array that doesn't contain it", () => {
          const ligneCommandeClient: ILigneCommandeClient = { id: 123 };
          const ligneCommandeClientCollection: ILigneCommandeClient[] = [{ id: 456 }];
          expectedResult = service.addLigneCommandeClientToCollectionIfMissing(ligneCommandeClientCollection, ligneCommandeClient);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(ligneCommandeClient);
        });

        it('should add only unique LigneCommandeClient to an array', () => {
          const ligneCommandeClientArray: ILigneCommandeClient[] = [{ id: 123 }, { id: 456 }, { id: 23430 }];
          const ligneCommandeClientCollection: ILigneCommandeClient[] = [{ id: 123 }];
          expectedResult = service.addLigneCommandeClientToCollectionIfMissing(ligneCommandeClientCollection, ...ligneCommandeClientArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const ligneCommandeClient: ILigneCommandeClient = { id: 123 };
          const ligneCommandeClient2: ILigneCommandeClient = { id: 456 };
          expectedResult = service.addLigneCommandeClientToCollectionIfMissing([], ligneCommandeClient, ligneCommandeClient2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(ligneCommandeClient);
          expect(expectedResult).toContain(ligneCommandeClient2);
        });

        it('should accept null and undefined values', () => {
          const ligneCommandeClient: ILigneCommandeClient = { id: 123 };
          expectedResult = service.addLigneCommandeClientToCollectionIfMissing([], null, ligneCommandeClient, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(ligneCommandeClient);
        });

        it('should return initial array if no LigneCommandeClient is added', () => {
          const ligneCommandeClientCollection: ILigneCommandeClient[] = [{ id: 123 }];
          expectedResult = service.addLigneCommandeClientToCollectionIfMissing(ligneCommandeClientCollection, undefined, null);
          expect(expectedResult).toEqual(ligneCommandeClientCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
