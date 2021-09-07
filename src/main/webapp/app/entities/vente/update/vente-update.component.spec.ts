jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { VenteService } from '../service/vente.service';
import { IVente, Vente } from '../vente.model';

import { VenteUpdateComponent } from './vente-update.component';

describe('Component Tests', () => {
  describe('Vente Management Update Component', () => {
    let comp: VenteUpdateComponent;
    let fixture: ComponentFixture<VenteUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let venteService: VenteService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [VenteUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(VenteUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(VenteUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      venteService = TestBed.inject(VenteService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const vente: IVente = { id: 456 };

        activatedRoute.data = of({ vente });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(vente));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Vente>>();
        const vente = { id: 123 };
        jest.spyOn(venteService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ vente });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: vente }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(venteService.update).toHaveBeenCalledWith(vente);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Vente>>();
        const vente = new Vente();
        jest.spyOn(venteService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ vente });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: vente }));
        saveSubject.complete();

        // THEN
        expect(venteService.create).toHaveBeenCalledWith(vente);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Vente>>();
        const vente = { id: 123 };
        jest.spyOn(venteService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ vente });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(venteService.update).toHaveBeenCalledWith(vente);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
