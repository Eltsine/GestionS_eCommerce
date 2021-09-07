import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LigneCommandeFournisseurDetailComponent } from './ligne-commande-fournisseur-detail.component';

describe('Component Tests', () => {
  describe('LigneCommandeFournisseur Management Detail Component', () => {
    let comp: LigneCommandeFournisseurDetailComponent;
    let fixture: ComponentFixture<LigneCommandeFournisseurDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [LigneCommandeFournisseurDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ ligneCommandeFournisseur: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(LigneCommandeFournisseurDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(LigneCommandeFournisseurDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load ligneCommandeFournisseur on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.ligneCommandeFournisseur).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
