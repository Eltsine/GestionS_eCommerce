import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MvtStkDetailComponent } from './mvt-stk-detail.component';

describe('Component Tests', () => {
  describe('MvtStk Management Detail Component', () => {
    let comp: MvtStkDetailComponent;
    let fixture: ComponentFixture<MvtStkDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [MvtStkDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ mvtStk: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(MvtStkDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(MvtStkDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load mvtStk on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.mvtStk).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
