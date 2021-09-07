jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { LigneVenteService } from '../service/ligne-vente.service';
import { ILigneVente, LigneVente } from '../ligne-vente.model';
import { IVente } from 'app/entities/vente/vente.model';
import { VenteService } from 'app/entities/vente/service/vente.service';

import { LigneVenteUpdateComponent } from './ligne-vente-update.component';

describe('Component Tests', () => {
  describe('LigneVente Management Update Component', () => {
    let comp: LigneVenteUpdateComponent;
    let fixture: ComponentFixture<LigneVenteUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let ligneVenteService: LigneVenteService;
    let venteService: VenteService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [LigneVenteUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(LigneVenteUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LigneVenteUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      ligneVenteService = TestBed.inject(LigneVenteService);
      venteService = TestBed.inject(VenteService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Vente query and add missing value', () => {
        const ligneVente: ILigneVente = { id: 456 };
        const vente: IVente = { id: 87627 };
        ligneVente.vente = vente;

        const venteCollection: IVente[] = [{ id: 6076 }];
        jest.spyOn(venteService, 'query').mockReturnValue(of(new HttpResponse({ body: venteCollection })));
        const additionalVentes = [vente];
        const expectedCollection: IVente[] = [...additionalVentes, ...venteCollection];
        jest.spyOn(venteService, 'addVenteToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ ligneVente });
        comp.ngOnInit();

        expect(venteService.query).toHaveBeenCalled();
        expect(venteService.addVenteToCollectionIfMissing).toHaveBeenCalledWith(venteCollection, ...additionalVentes);
        expect(comp.ventesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const ligneVente: ILigneVente = { id: 456 };
        const vente: IVente = { id: 67741 };
        ligneVente.vente = vente;

        activatedRoute.data = of({ ligneVente });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(ligneVente));
        expect(comp.ventesSharedCollection).toContain(vente);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<LigneVente>>();
        const ligneVente = { id: 123 };
        jest.spyOn(ligneVenteService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ ligneVente });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: ligneVente }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(ligneVenteService.update).toHaveBeenCalledWith(ligneVente);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<LigneVente>>();
        const ligneVente = new LigneVente();
        jest.spyOn(ligneVenteService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ ligneVente });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: ligneVente }));
        saveSubject.complete();

        // THEN
        expect(ligneVenteService.create).toHaveBeenCalledWith(ligneVente);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<LigneVente>>();
        const ligneVente = { id: 123 };
        jest.spyOn(ligneVenteService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ ligneVente });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(ligneVenteService.update).toHaveBeenCalledWith(ligneVente);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackVenteById', () => {
        it('Should return tracked Vente primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackVenteById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
