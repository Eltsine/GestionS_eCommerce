jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { LigneCommandeClientService } from '../service/ligne-commande-client.service';
import { ILigneCommandeClient, LigneCommandeClient } from '../ligne-commande-client.model';
import { ICommandeClient } from 'app/entities/commande-client/commande-client.model';
import { CommandeClientService } from 'app/entities/commande-client/service/commande-client.service';

import { LigneCommandeClientUpdateComponent } from './ligne-commande-client-update.component';

describe('Component Tests', () => {
  describe('LigneCommandeClient Management Update Component', () => {
    let comp: LigneCommandeClientUpdateComponent;
    let fixture: ComponentFixture<LigneCommandeClientUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let ligneCommandeClientService: LigneCommandeClientService;
    let commandeClientService: CommandeClientService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [LigneCommandeClientUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(LigneCommandeClientUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LigneCommandeClientUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      ligneCommandeClientService = TestBed.inject(LigneCommandeClientService);
      commandeClientService = TestBed.inject(CommandeClientService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call CommandeClient query and add missing value', () => {
        const ligneCommandeClient: ILigneCommandeClient = { id: 456 };
        const commandeClient: ICommandeClient = { id: 25032 };
        ligneCommandeClient.commandeClient = commandeClient;

        const commandeClientCollection: ICommandeClient[] = [{ id: 4334 }];
        jest.spyOn(commandeClientService, 'query').mockReturnValue(of(new HttpResponse({ body: commandeClientCollection })));
        const additionalCommandeClients = [commandeClient];
        const expectedCollection: ICommandeClient[] = [...additionalCommandeClients, ...commandeClientCollection];
        jest.spyOn(commandeClientService, 'addCommandeClientToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ ligneCommandeClient });
        comp.ngOnInit();

        expect(commandeClientService.query).toHaveBeenCalled();
        expect(commandeClientService.addCommandeClientToCollectionIfMissing).toHaveBeenCalledWith(
          commandeClientCollection,
          ...additionalCommandeClients
        );
        expect(comp.commandeClientsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const ligneCommandeClient: ILigneCommandeClient = { id: 456 };
        const commandeClient: ICommandeClient = { id: 55355 };
        ligneCommandeClient.commandeClient = commandeClient;

        activatedRoute.data = of({ ligneCommandeClient });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(ligneCommandeClient));
        expect(comp.commandeClientsSharedCollection).toContain(commandeClient);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<LigneCommandeClient>>();
        const ligneCommandeClient = { id: 123 };
        jest.spyOn(ligneCommandeClientService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ ligneCommandeClient });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: ligneCommandeClient }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(ligneCommandeClientService.update).toHaveBeenCalledWith(ligneCommandeClient);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<LigneCommandeClient>>();
        const ligneCommandeClient = new LigneCommandeClient();
        jest.spyOn(ligneCommandeClientService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ ligneCommandeClient });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: ligneCommandeClient }));
        saveSubject.complete();

        // THEN
        expect(ligneCommandeClientService.create).toHaveBeenCalledWith(ligneCommandeClient);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<LigneCommandeClient>>();
        const ligneCommandeClient = { id: 123 };
        jest.spyOn(ligneCommandeClientService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ ligneCommandeClient });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(ligneCommandeClientService.update).toHaveBeenCalledWith(ligneCommandeClient);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackCommandeClientById', () => {
        it('Should return tracked CommandeClient primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackCommandeClientById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
