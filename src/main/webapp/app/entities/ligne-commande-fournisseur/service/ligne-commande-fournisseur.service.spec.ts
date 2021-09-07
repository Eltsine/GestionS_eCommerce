import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ILigneCommandeFournisseur, LigneCommandeFournisseur } from '../ligne-commande-fournisseur.model';

import { LigneCommandeFournisseurService } from './ligne-commande-fournisseur.service';

describe('Service Tests', () => {
  describe('LigneCommandeFournisseur Service', () => {
    let service: LigneCommandeFournisseurService;
    let httpMock: HttpTestingController;
    let elemDefault: ILigneCommandeFournisseur;
    let expectedResult: ILigneCommandeFournisseur | ILigneCommandeFournisseur[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(LigneCommandeFournisseurService);
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

      it('should create a LigneCommandeFournisseur', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new LigneCommandeFournisseur()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a LigneCommandeFournisseur', () => {
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

      it('should partial update a LigneCommandeFournisseur', () => {
        const patchObject = Object.assign({}, new LigneCommandeFournisseur());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of LigneCommandeFournisseur', () => {
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

      it('should delete a LigneCommandeFournisseur', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addLigneCommandeFournisseurToCollectionIfMissing', () => {
        it('should add a LigneCommandeFournisseur to an empty array', () => {
          const ligneCommandeFournisseur: ILigneCommandeFournisseur = { id: 123 };
          expectedResult = service.addLigneCommandeFournisseurToCollectionIfMissing([], ligneCommandeFournisseur);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(ligneCommandeFournisseur);
        });

        it('should not add a LigneCommandeFournisseur to an array that contains it', () => {
          const ligneCommandeFournisseur: ILigneCommandeFournisseur = { id: 123 };
          const ligneCommandeFournisseurCollection: ILigneCommandeFournisseur[] = [
            {
              ...ligneCommandeFournisseur,
            },
            { id: 456 },
          ];
          expectedResult = service.addLigneCommandeFournisseurToCollectionIfMissing(
            ligneCommandeFournisseurCollection,
            ligneCommandeFournisseur
          );
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a LigneCommandeFournisseur to an array that doesn't contain it", () => {
          const ligneCommandeFournisseur: ILigneCommandeFournisseur = { id: 123 };
          const ligneCommandeFournisseurCollection: ILigneCommandeFournisseur[] = [{ id: 456 }];
          expectedResult = service.addLigneCommandeFournisseurToCollectionIfMissing(
            ligneCommandeFournisseurCollection,
            ligneCommandeFournisseur
          );
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(ligneCommandeFournisseur);
        });

        it('should add only unique LigneCommandeFournisseur to an array', () => {
          const ligneCommandeFournisseurArray: ILigneCommandeFournisseur[] = [{ id: 123 }, { id: 456 }, { id: 34430 }];
          const ligneCommandeFournisseurCollection: ILigneCommandeFournisseur[] = [{ id: 123 }];
          expectedResult = service.addLigneCommandeFournisseurToCollectionIfMissing(
            ligneCommandeFournisseurCollection,
            ...ligneCommandeFournisseurArray
          );
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const ligneCommandeFournisseur: ILigneCommandeFournisseur = { id: 123 };
          const ligneCommandeFournisseur2: ILigneCommandeFournisseur = { id: 456 };
          expectedResult = service.addLigneCommandeFournisseurToCollectionIfMissing(
            [],
            ligneCommandeFournisseur,
            ligneCommandeFournisseur2
          );
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(ligneCommandeFournisseur);
          expect(expectedResult).toContain(ligneCommandeFournisseur2);
        });

        it('should accept null and undefined values', () => {
          const ligneCommandeFournisseur: ILigneCommandeFournisseur = { id: 123 };
          expectedResult = service.addLigneCommandeFournisseurToCollectionIfMissing([], null, ligneCommandeFournisseur, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(ligneCommandeFournisseur);
        });

        it('should return initial array if no LigneCommandeFournisseur is added', () => {
          const ligneCommandeFournisseurCollection: ILigneCommandeFournisseur[] = [{ id: 123 }];
          expectedResult = service.addLigneCommandeFournisseurToCollectionIfMissing(ligneCommandeFournisseurCollection, undefined, null);
          expect(expectedResult).toEqual(ligneCommandeFournisseurCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
