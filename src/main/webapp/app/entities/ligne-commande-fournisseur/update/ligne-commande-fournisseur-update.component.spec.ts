jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { LigneCommandeFournisseurService } from '../service/ligne-commande-fournisseur.service';
import { ILigneCommandeFournisseur, LigneCommandeFournisseur } from '../ligne-commande-fournisseur.model';
import { ICommandeFournisseur } from 'app/entities/commande-fournisseur/commande-fournisseur.model';
import { CommandeFournisseurService } from 'app/entities/commande-fournisseur/service/commande-fournisseur.service';

import { LigneCommandeFournisseurUpdateComponent } from './ligne-commande-fournisseur-update.component';

describe('Component Tests', () => {
  describe('LigneCommandeFournisseur Management Update Component', () => {
    let comp: LigneCommandeFournisseurUpdateComponent;
    let fixture: ComponentFixture<LigneCommandeFournisseurUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let ligneCommandeFournisseurService: LigneCommandeFournisseurService;
    let commandeFournisseurService: CommandeFournisseurService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [LigneCommandeFournisseurUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(LigneCommandeFournisseurUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LigneCommandeFournisseurUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      ligneCommandeFournisseurService = TestBed.inject(LigneCommandeFournisseurService);
      commandeFournisseurService = TestBed.inject(CommandeFournisseurService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call CommandeFournisseur query and add missing value', () => {
        const ligneCommandeFournisseur: ILigneCommandeFournisseur = { id: 456 };
        const commandeFournisseur: ICommandeFournisseur = { id: 37786 };
        ligneCommandeFournisseur.commandeFournisseur = commandeFournisseur;

        const commandeFournisseurCollection: ICommandeFournisseur[] = [{ id: 19792 }];
        jest.spyOn(commandeFournisseurService, 'query').mockReturnValue(of(new HttpResponse({ body: commandeFournisseurCollection })));
        const additionalCommandeFournisseurs = [commandeFournisseur];
        const expectedCollection: ICommandeFournisseur[] = [...additionalCommandeFournisseurs, ...commandeFournisseurCollection];
        jest.spyOn(commandeFournisseurService, 'addCommandeFournisseurToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ ligneCommandeFournisseur });
        comp.ngOnInit();

        expect(commandeFournisseurService.query).toHaveBeenCalled();
        expect(commandeFournisseurService.addCommandeFournisseurToCollectionIfMissing).toHaveBeenCalledWith(
          commandeFournisseurCollection,
          ...additionalCommandeFournisseurs
        );
        expect(comp.commandeFournisseursSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const ligneCommandeFournisseur: ILigneCommandeFournisseur = { id: 456 };
        const commandeFournisseur: ICommandeFournisseur = { id: 79490 };
        ligneCommandeFournisseur.commandeFournisseur = commandeFournisseur;

        activatedRoute.data = of({ ligneCommandeFournisseur });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(ligneCommandeFournisseur));
        expect(comp.commandeFournisseursSharedCollection).toContain(commandeFournisseur);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<LigneCommandeFournisseur>>();
        const ligneCommandeFournisseur = { id: 123 };
        jest.spyOn(ligneCommandeFournisseurService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ ligneCommandeFournisseur });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: ligneCommandeFournisseur }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(ligneCommandeFournisseurService.update).toHaveBeenCalledWith(ligneCommandeFournisseur);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<LigneCommandeFournisseur>>();
        const ligneCommandeFournisseur = new LigneCommandeFournisseur();
        jest.spyOn(ligneCommandeFournisseurService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ ligneCommandeFournisseur });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: ligneCommandeFournisseur }));
        saveSubject.complete();

        // THEN
        expect(ligneCommandeFournisseurService.create).toHaveBeenCalledWith(ligneCommandeFournisseur);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<LigneCommandeFournisseur>>();
        const ligneCommandeFournisseur = { id: 123 };
        jest.spyOn(ligneCommandeFournisseurService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ ligneCommandeFournisseur });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(ligneCommandeFournisseurService.update).toHaveBeenCalledWith(ligneCommandeFournisseur);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackCommandeFournisseurById', () => {
        it('Should return tracked CommandeFournisseur primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackCommandeFournisseurById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
