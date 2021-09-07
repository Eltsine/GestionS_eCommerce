jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { MvtStkService } from '../service/mvt-stk.service';

import { MvtStkDeleteDialogComponent } from './mvt-stk-delete-dialog.component';

describe('Component Tests', () => {
  describe('MvtStk Management Delete Component', () => {
    let comp: MvtStkDeleteDialogComponent;
    let fixture: ComponentFixture<MvtStkDeleteDialogComponent>;
    let service: MvtStkService;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [MvtStkDeleteDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(MvtStkDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(MvtStkDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(MvtStkService);
      mockActiveModal = TestBed.inject(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({})));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
        })
      ));

      it('Should not call delete service on clear', () => {
        // GIVEN
        jest.spyOn(service, 'delete');

        // WHEN
        comp.cancel();

        // THEN
        expect(service.delete).not.toHaveBeenCalled();
        expect(mockActiveModal.close).not.toHaveBeenCalled();
        expect(mockActiveModal.dismiss).toHaveBeenCalled();
      });
    });
  });
});
