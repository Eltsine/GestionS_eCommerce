import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DataUtils } from 'app/core/util/data-util.service';

import { ArticleDetailComponent } from './article-detail.component';

describe('Component Tests', () => {
  describe('Article Management Detail Component', () => {
    let comp: ArticleDetailComponent;
    let fixture: ComponentFixture<ArticleDetailComponent>;
    let dataUtils: DataUtils;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ArticleDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ article: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ArticleDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ArticleDetailComponent);
      comp = fixture.componentInstance;
      dataUtils = TestBed.inject(DataUtils);
      jest.spyOn(window, 'open').mockImplementation(() => null);
    });

    describe('OnInit', () => {
      it('Should load article on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.article).toEqual(expect.objectContaining({ id: 123 }));
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