jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { AdresseService } from '../service/adresse.service';
import { IAdresse, Adresse } from '../adresse.model';

import { AdresseUpdateComponent } from './adresse-update.component';

describe('Component Tests', () => {
  describe('Adresse Management Update Component', () => {
    let comp: AdresseUpdateComponent;
    let fixture: ComponentFixture<AdresseUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let adresseService: AdresseService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [AdresseUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(AdresseUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AdresseUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      adresseService = TestBed.inject(AdresseService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const adresse: IAdresse = { id: 456 };

        activatedRoute.data = of({ adresse });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(adresse));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Adresse>>();
        const adresse = { id: 123 };
        jest.spyOn(adresseService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ adresse });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: adresse }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(adresseService.update).toHaveBeenCalledWith(adresse);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Adresse>>();
        const adresse = new Adresse();
        jest.spyOn(adresseService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ adresse });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: adresse }));
        saveSubject.complete();

        // THEN
        expect(adresseService.create).toHaveBeenCalledWith(adresse);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Adresse>>();
        const adresse = { id: 123 };
        jest.spyOn(adresseService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ adresse });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(adresseService.update).toHaveBeenCalledWith(adresse);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
