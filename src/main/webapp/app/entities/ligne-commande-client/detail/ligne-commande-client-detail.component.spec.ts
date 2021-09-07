import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LigneCommandeClientDetailComponent } from './ligne-commande-client-detail.component';

describe('Component Tests', () => {
  describe('LigneCommandeClient Management Detail Component', () => {
    let comp: LigneCommandeClientDetailComponent;
    let fixture: ComponentFixture<LigneCommandeClientDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [LigneCommandeClientDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ ligneCommandeClient: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(LigneCommandeClientDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(LigneCommandeClientDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load ligneCommandeClient on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.ligneCommandeClient).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
