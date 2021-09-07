import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LigneVenteDetailComponent } from './ligne-vente-detail.component';

describe('Component Tests', () => {
  describe('LigneVente Management Detail Component', () => {
    let comp: LigneVenteDetailComponent;
    let fixture: ComponentFixture<LigneVenteDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [LigneVenteDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ ligneVente: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(LigneVenteDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(LigneVenteDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load ligneVente on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.ligneVente).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
