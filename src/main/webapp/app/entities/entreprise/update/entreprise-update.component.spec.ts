jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { EntrepriseService } from '../service/entreprise.service';
import { IEntreprise, Entreprise } from '../entreprise.model';
import { IAdresse } from 'app/entities/adresse/adresse.model';
import { AdresseService } from 'app/entities/adresse/service/adresse.service';

import { EntrepriseUpdateComponent } from './entreprise-update.component';

describe('Component Tests', () => {
  describe('Entreprise Management Update Component', () => {
    let comp: EntrepriseUpdateComponent;
    let fixture: ComponentFixture<EntrepriseUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let entrepriseService: EntrepriseService;
    let adresseService: AdresseService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [EntrepriseUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(EntrepriseUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(EntrepriseUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      entrepriseService = TestBed.inject(EntrepriseService);
      adresseService = TestBed.inject(AdresseService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Adresse query and add missing value', () => {
        const entreprise: IEntreprise = { id: 456 };
        const adresse: IAdresse = { id: 48930 };
        entreprise.adresse = adresse;

        const adresseCollection: IAdresse[] = [{ id: 62197 }];
        jest.spyOn(adresseService, 'query').mockReturnValue(of(new HttpResponse({ body: adresseCollection })));
        const additionalAdresses = [adresse];
        const expectedCollection: IAdresse[] = [...additionalAdresses, ...adresseCollection];
        jest.spyOn(adresseService, 'addAdresseToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ entreprise });
        comp.ngOnInit();

        expect(adresseService.query).toHaveBeenCalled();
        expect(adresseService.addAdresseToCollectionIfMissing).toHaveBeenCalledWith(adresseCollection, ...additionalAdresses);
        expect(comp.adressesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const entreprise: IEntreprise = { id: 456 };
        const adresse: IAdresse = { id: 68952 };
        entreprise.adresse = adresse;

        activatedRoute.data = of({ entreprise });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(entreprise));
        expect(comp.adressesSharedCollection).toContain(adresse);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Entreprise>>();
        const entreprise = { id: 123 };
        jest.spyOn(entrepriseService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ entreprise });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: entreprise }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(entrepriseService.update).toHaveBeenCalledWith(entreprise);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Entreprise>>();
        const entreprise = new Entreprise();
        jest.spyOn(entrepriseService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ entreprise });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: entreprise }));
        saveSubject.complete();

        // THEN
        expect(entrepriseService.create).toHaveBeenCalledWith(entreprise);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Entreprise>>();
        const entreprise = { id: 123 };
        jest.spyOn(entrepriseService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ entreprise });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(entrepriseService.update).toHaveBeenCalledWith(entreprise);
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
