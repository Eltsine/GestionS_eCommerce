import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DataUtils } from 'app/core/util/data-util.service';

import { EntrepriseDetailComponent } from './entreprise-detail.component';

describe('Component Tests', () => {
  describe('Entreprise Management Detail Component', () => {
    let comp: EntrepriseDetailComponent;
    let fixture: ComponentFixture<EntrepriseDetailComponent>;
    let dataUtils: DataUtils;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [EntrepriseDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ entreprise: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(EntrepriseDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(EntrepriseDetailComponent);
      comp = fixture.componentInstance;
      dataUtils = TestBed.inject(DataUtils);
      jest.spyOn(window, 'open').mockImplementation(() => null);
    });

    describe('OnInit', () => {
      it('Should load entreprise on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.entreprise).toEqual(expect.objectContaining({ id: 123 }));
      });
    });

    describe('byteSize', () => {
      it('Should call byteSize from DataUtils', () => {
        // GIVEN
        jest.spyOn(dataUtils, 'byteSize');
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.byteSize(fakeBase64);

        // THEN
        expect(dataUtils.byteSize).toBeCalledWith(fakeBase64);
      });
    });

    describe('openFile', () => {
      it('Should call openFile from DataUtils', () => {
        // GIVEN
        jest.spyOn(dataUtils, 'openFile');
        const fakeContentType = 'fake content type';
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.openFile(fakeBase64, fakeContentType);

        // THEN
        expect(dataUtils.openFile).toBeCalledWith(fakeBase64, fakeContentType);
      });
    });
  });
});
