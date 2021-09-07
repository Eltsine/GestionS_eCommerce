jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IMvtStk, MvtStk } from '../mvt-stk.model';
import { MvtStkService } from '../service/mvt-stk.service';

import { MvtStkRoutingResolveService } from './mvt-stk-routing-resolve.service';

describe('Service Tests', () => {
  describe('MvtStk routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: MvtStkRoutingResolveService;
    let service: MvtStkService;
    let resultMvtStk: IMvtStk | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(MvtStkRoutingResolveService);
      service = TestBed.inject(MvtStkService);
      resultMvtStk = undefined;
    });

    describe('resolve', () => {
      it('should return IMvtStk returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultMvtStk = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultMvtStk).toEqual({ id: 123 });
      });

      it('should return new IMvtStk if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultMvtStk = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultMvtStk).toEqual(new MvtStk());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as MvtStk })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultMvtStk = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultMvtStk).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
