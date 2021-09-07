jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { FournisseurService } from '../service/fournisseur.service';
import { IFournisseur, Fournisseur } from '../fournisseur.model';
import { IAdresse } from 'app/entities/adresse/adresse.model';
import { AdresseService } from 'app/entities/adresse/service/adresse.service';

import { FournisseurUpdateComponent } from './fournisseur-update.component';

describe('Component Tests', () => {
  describe('Fournisseur Management Update Component', () => {
    let comp: FournisseurUpdateComponent;
    let fixture: ComponentFixture<FournisseurUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let fournisseurService: FournisseurService;
    let adresseService: AdresseService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [FournisseurUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(FournisseurUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FournisseurUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      fournisseurService = TestBed.inject(FournisseurService);
      adresseService = TestBed.inject(AdresseService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Adresse query and add missing value', () => {
        const fournisseur: IFournisseur = { id: 456 };
        const adresse: IAdresse = { id: 66065 };
        fournisseur.adresse = adresse;

        const adresseCollection: IAdresse[] = [{ id: 51758 }];
        jest.spyOn(adresseService, 'query').mockReturnValue(of(new HttpResponse({ body: adresseCollection })));
        const additionalAdresses = [adresse];
        const expectedCollection: IAdresse[] = [...additionalAdresses, ...adresseCollection];
        jest.spyOn(adresseService, 'addAdresseToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ fournisseur });
        comp.ngOnInit();

        expect(adresseService.query).toHaveBeenCalled();
        expect(adresseService.addAdresseToCollectionIfMissing).toHaveBeenCalledWith(adresseCollection, ...additionalAdresses);
        expect(comp.adressesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const fournisseur: IFournisseur = { id: 456 };
        const adresse: IAdresse = { id: 77852 };
        fournisseur.adresse = adresse;

        activatedRoute.data = of({ fournisseur });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(fournisseur));
        expect(comp.adressesSharedCollection).toContain(adresse);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Fournisseur>>();
        const fournisseur = { id: 123 };
        jest.spyOn(fournisseurService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ fournisseur });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: fournisseur }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(fournisseurService.update).toHaveBeenCalledWith(fournisseur);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Fournisseur>>();
        const fournisseur = new Fournisseur();
        jest.spyOn(fournisseurService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ fournisseur });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: fournisseur }));
        saveSubject.complete();

        // THEN
        expect(fournisseurService.create).toHaveBeenCalledWith(fournisseur);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Fournisseur>>();
        const fournisseur = { id: 123 };
        jest.spyOn(fournisseurService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ fournisseur });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(fournisseurService.update).toHaveBeenCalledWith(fournisseur);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackAdresseById', () => {
        it('Should return tracked Adresse primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackAdresseById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
