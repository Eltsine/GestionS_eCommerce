import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ILigneVente, LigneVente } from '../ligne-vente.model';

import { LigneVenteService } from './ligne-vente.service';

describe('Service Tests', () => {
  describe('LigneVente Service', () => {
    let service: LigneVenteService;
    let httpMock: HttpTestingController;
    let elemDefault: ILigneVente;
    let expectedResult: ILigneVente | ILigneVente[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(LigneVenteService);
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

      it('should create a LigneVente', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new LigneVente()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a LigneVente', () => {
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

      it('should partial update a LigneVente', () => {
        const patchObject = Object.assign({}, new LigneVente());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of LigneVente', () => {
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

      it('should delete a LigneVente', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addLigneVenteToCollectionIfMissing', () => {
        it('should add a LigneVente to an empty array', () => {
          const ligneVente: ILigneVente = { id: 123 };
          expectedResult = service.addLigneVenteToCollectionIfMissing([], ligneVente);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(ligneVente);
        });

        it('should not add a LigneVente to an array that contains it', () => {
          const ligneVente: ILigneVente = { id: 123 };
          const ligneVenteCollection: ILigneVente[] = [
            {
              ...ligneVente,
            },
            { id: 456 },
          ];
          expectedResult = service.addLigneVenteToCollectionIfMissing(ligneVenteCollection, ligneVente);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a LigneVente to an array that doesn't contain it", () => {
          const ligneVente: ILigneVente = { id: 123 };
          const ligneVenteCollection: ILigneVente[] = [{ id: 456 }];
          expectedResult = service.addLigneVenteToCollectionIfMissing(ligneVenteCollection, ligneVente);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(ligneVente);
        });

        it('should add only unique LigneVente to an array', () => {
          const ligneVenteArray: ILigneVente[] = [{ id: 123 }, { id: 456 }, { id: 56806 }];
          const ligneVenteCollection: ILigneVente[] = [{ id: 123 }];
          expectedResult = service.addLigneVenteToCollectionIfMissing(ligneVenteCollection, ...ligneVenteArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const ligneVente: ILigneVente = { id: 123 };
          const ligneVente2: ILigneVente = { id: 456 };
          expectedResult = service.addLigneVenteToCollectionIfMissing([], ligneVente, ligneVente2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(ligneVente);
          expect(expectedResult).toContain(ligneVente2);
        });

        it('should accept null and undefined values', () => {
          const ligneVente: ILigneVente = { id: 123 };
          expectedResult = service.addLigneVenteToCollectionIfMissing([], null, ligneVente, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(ligneVente);
        });

        it('should return initial array if no LigneVente is added', () => {
          const ligneVenteCollection: ILigneVente[] = [{ id: 123 }];
          expectedResult = service.addLigneVenteToCollectionIfMissing(ligneVenteCollection, undefined, null);
          expect(expectedResult).toEqual(ligneVenteCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
