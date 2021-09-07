jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PersonnelService } from '../service/personnel.service';
import { IPersonnel, Personnel } from '../personnel.model';
import { IAdresse } from 'app/entities/adresse/adresse.model';
import { AdresseService } from 'app/entities/adresse/service/adresse.service';
import { IEntreprise } from 'app/entities/entreprise/entreprise.model';
import { EntrepriseService } from 'app/entities/entreprise/service/entreprise.service';

import { PersonnelUpdateComponent } from './personnel-update.component';

describe('Component Tests', () => {
  describe('Personnel Management Update Component', () => {
    let comp: PersonnelUpdateComponent;
    let fixture: ComponentFixture<PersonnelUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let personnelService: PersonnelService;
    let adresseService: AdresseService;
    let entrepriseService: EntrepriseService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PersonnelUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(PersonnelUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PersonnelUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      personnelService = TestBed.inject(PersonnelService);
      adresseService = TestBed.inject(AdresseService);
      entrepriseService = TestBed.inject(EntrepriseService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Adresse query and add missing value', () => {
        const personnel: IPersonnel = { id: 456 };
        const adresse: IAdresse = { id: 96847 };
        personnel.adresse = adresse;

        const adresseCollection: IAdresse[] = [{ id: 55687 }];
        jest.spyOn(adresseService, 'query').mockReturnValue(of(new HttpResponse({ body: adresseCollection })));
        const additionalAdresses = [adresse];
        const expectedCollection: IAdresse[] = [...additionalAdresses, ...adresseCollection];
        jest.spyOn(adresseService, 'addAdresseToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ personnel });
        comp.ngOnInit();

        expect(adresseService.query).toHaveBeenCalled();
        expect(adresseService.addAdresseToCollectionIfMissing).toHaveBeenCalledWith(adresseCollection, ...additionalAdresses);
        expect(comp.adressesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Entreprise query and add missing value', () => {
        const personnel: IPersonnel = { id: 456 };
        const entreprise: IEntreprise = { id: 14071 };
        personnel.entreprise = entreprise;

        const entrepriseCollection: IEntreprise[] = [{ id: 40669 }];
        jest.spyOn(entrepriseService, 'query').mockReturnValue(of(new HttpResponse({ body: entrepriseCollection })));
        const additionalEntreprises = [entreprise];
        const expectedCollection: IEntreprise[] = [...additionalEntreprises, ...entrepriseCollection];
        jest.spyOn(entrepriseService, 'addEntrepriseToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ personnel });
        comp.ngOnInit();

        expect(entrepriseService.query).toHaveBeenCalled();
        expect(entrepriseService.addEntrepriseToCollectionIfMissing).toHaveBeenCalledWith(entrepriseCollection, ...additionalEntreprises);
        expect(comp.entreprisesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const personnel: IPersonnel = { id: 456 };
        const adresse: IAdresse = { id: 13005 };
        personnel.adresse = adresse;
        const entreprise: IEntreprise = { id: 12157 };
        personnel.entreprise = entreprise;

        activatedRoute.data = of({ personnel });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(personnel));
        expect(comp.adressesSharedCollection).toContain(adresse);
        expect(comp.entreprisesSharedCollection).toContain(entreprise);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Personnel>>();
        const personnel = { id: 123 };
        jest.spyOn(personnelService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ personnel });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: personnel }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(personnelService.update).toHaveBeenCalledWith(personnel);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Personnel>>();
        const personnel = new Personnel();
        jest.spyOn(personnelService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ personnel });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: personnel }));
        saveSubject.complete();

        // THEN
        expect(personnelService.create).toHaveBeenCalledWith(personnel);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Personnel>>();
        const personnel = { id: 123 };
        jest.spyOn(personnelService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ personnel });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(personnelService.update).toHaveBeenCalledWith(personnel);
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

      describe('trackEntrepriseById', () => {
        it('Should return tracked Entreprise primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackEntrepriseById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
