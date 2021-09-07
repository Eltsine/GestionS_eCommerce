jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IPersonnel, Personnel } from '../personnel.model';
import { PersonnelService } from '../service/personnel.service';

import { PersonnelRoutingResolveService } from './personnel-routing-resolve.service';

describe('Service Tests', () => {
  describe('Personnel routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: PersonnelRoutingResolveService;
    let service: PersonnelService;
    let resultPersonnel: IPersonnel | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(PersonnelRoutingResolveService);
      service = TestBed.inject(PersonnelService);
      resultPersonnel = undefined;
    });

    describe('resolve', () => {
      it('should return IPersonnel returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPersonnel = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPersonnel).toEqual({ id: 123 });
      });

      it('should return new IPersonnel if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPersonnel = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultPersonnel).toEqual(new Personnel());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Personnel })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPersonnel = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPersonnel).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
