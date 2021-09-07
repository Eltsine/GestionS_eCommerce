jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ILigneCommandeFournisseur, LigneCommandeFournisseur } from '../ligne-commande-fournisseur.model';
import { LigneCommandeFournisseurService } from '../service/ligne-commande-fournisseur.service';

import { LigneCommandeFournisseurRoutingResolveService } from './ligne-commande-fournisseur-routing-resolve.service';

describe('Service Tests', () => {
  describe('LigneCommandeFournisseur routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: LigneCommandeFournisseurRoutingResolveService;
    let service: LigneCommandeFournisseurService;
    let resultLigneCommandeFournisseur: ILigneCommandeFournisseur | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(LigneCommandeFournisseurRoutingResolveService);
      service = TestBed.inject(LigneCommandeFournisseurService);
      resultLigneCommandeFournisseur = undefined;
    });

    describe('resolve', () => {
      it('should return ILigneCommandeFournisseur returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLigneCommandeFournisseur = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultLigneCommandeFournisseur).toEqual({ id: 123 });
      });

      it('should return new ILigneCommandeFournisseur if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLigneCommandeFournisseur = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultLigneCommandeFournisseur).toEqual(new LigneCommandeFournisseur());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as LigneCommandeFournisseur })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLigneCommandeFournisseur = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultLigneCommandeFournisseur).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
