jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ILigneVente, LigneVente } from '../ligne-vente.model';
import { LigneVenteService } from '../service/ligne-vente.service';

import { LigneVenteRoutingResolveService } from './ligne-vente-routing-resolve.service';

describe('Service Tests', () => {
  describe('LigneVente routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: LigneVenteRoutingResolveService;
    let service: LigneVenteService;
    let resultLigneVente: ILigneVente | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(LigneVenteRoutingResolveService);
      service = TestBed.inject(LigneVenteService);
      resultLigneVente = undefined;
    });

    describe('resolve', () => {
      it('should return ILigneVente returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLigneVente = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultLigneVente).toEqual({ id: 123 });
      });

      it('should return new ILigneVente if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLigneVente = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultLigneVente).toEqual(new LigneVente());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as LigneVente })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLigneVente = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultLigneVente).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
