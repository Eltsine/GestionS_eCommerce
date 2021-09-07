jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ILigneCommandeClient, LigneCommandeClient } from '../ligne-commande-client.model';
import { LigneCommandeClientService } from '../service/ligne-commande-client.service';

import { LigneCommandeClientRoutingResolveService } from './ligne-commande-client-routing-resolve.service';

describe('Service Tests', () => {
  describe('LigneCommandeClient routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: LigneCommandeClientRoutingResolveService;
    let service: LigneCommandeClientService;
    let resultLigneCommandeClient: ILigneCommandeClient | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(LigneCommandeClientRoutingResolveService);
      service = TestBed.inject(LigneCommandeClientService);
      resultLigneCommandeClient = undefined;
    });

    describe('resolve', () => {
      it('should return ILigneCommandeClient returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLigneCommandeClient = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultLigneCommandeClient).toEqual({ id: 123 });
      });

      it('should return new ILigneCommandeClient if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLigneCommandeClient = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultLigneCommandeClient).toEqual(new LigneCommandeClient());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as LigneCommandeClient })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLigneCommandeClient = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultLigneCommandeClient).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
