jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ClientService } from '../service/client.service';
import { IClient, Client } from '../client.model';
import { IAdresse } from 'app/entities/adresse/adresse.model';
import { AdresseService } from 'app/entities/adresse/service/adresse.service';

import { ClientUpdateComponent } from './client-update.component';

describe('Component Tests', () => {
  describe('Client Management Update Component', () => {
    let comp: ClientUpdateComponent;
    let fixture: ComponentFixture<ClientUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let clientService: ClientService;
    let adresseService: AdresseService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ClientUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ClientUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ClientUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      clientService = TestBed.inject(ClientService);
      adresseService = TestBed.inject(AdresseService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Adresse query and add missing value', () => {
        const client: IClient = { id: 456 };
        const adresse: IAdresse = { id: 79896 };
        client.adresse = adresse;

        const adresseCollection: IAdresse[] = [{ id: 17849 }];
        jest.spyOn(adresseService, 'query').mockReturnValue(of(new HttpResponse({ body: adresseCollection })));
        const additionalAdresses = [adresse];
        const expectedCollection: IAdresse[] = [...additionalAdresses, ...adresseCollection];
        jest.spyOn(adresseService, 'addAdresseToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ client });
        comp.ngOnInit();

        expect(adresseService.query).toHaveBeenCalled();
        expect(adresseService.addAdresseToCollectionIfMissing).toHaveBeenCalledWith(adresseCollection, ...additionalAdresses);
        expect(comp.adressesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const client: IClient = { id: 456 };
        const adresse: IAdresse = { id: 4749 };
        client.adresse = adresse;

        activatedRoute.data = of({ client });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(client));
        expect(comp.adressesSharedCollection).toContain(adresse);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Client>>();
        const client = { id: 123 };
        jest.spyOn(clientService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ client });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: client }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(clientService.update).toHaveBeenCalledWith(client);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Client>>();
        const client = new Client();
        jest.spyOn(clientService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ client });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: client }));
        saveSubject.complete();

        // THEN
        expect(clientService.create).toHaveBeenCalledWith(client);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Client>>();
        const client = { id: 123 };
        jest.spyOn(clientService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ client });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(clientService.update).toHaveBeenCalledWith(client);
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
