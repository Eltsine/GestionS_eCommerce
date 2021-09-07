jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { MvtStkService } from '../service/mvt-stk.service';
import { IMvtStk, MvtStk } from '../mvt-stk.model';
import { IArticle } from 'app/entities/article/article.model';
import { ArticleService } from 'app/entities/article/service/article.service';

import { MvtStkUpdateComponent } from './mvt-stk-update.component';

describe('Component Tests', () => {
  describe('MvtStk Management Update Component', () => {
    let comp: MvtStkUpdateComponent;
    let fixture: ComponentFixture<MvtStkUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let mvtStkService: MvtStkService;
    let articleService: ArticleService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [MvtStkUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(MvtStkUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MvtStkUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      mvtStkService = TestBed.inject(MvtStkService);
      articleService = TestBed.inject(ArticleService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Article query and add missing value', () => {
        const mvtStk: IMvtStk = { id: 456 };
        const entreprise: IArticle = { id: 4935 };
        mvtStk.entreprise = entreprise;

        const articleCollection: IArticle[] = [{ id: 93543 }];
        jest.spyOn(articleService, 'query').mockReturnValue(of(new HttpResponse({ body: articleCollection })));
        const additionalArticles = [entreprise];
        const expectedCollection: IArticle[] = [...additionalArticles, ...articleCollection];
        jest.spyOn(articleService, 'addArticleToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ mvtStk });
        comp.ngOnInit();

        expect(articleService.query).toHaveBeenCalled();
        expect(articleService.addArticleToCollectionIfMissing).toHaveBeenCalledWith(articleCollection, ...additionalArticles);
        expect(comp.articlesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const mvtStk: IMvtStk = { id: 456 };
        const entreprise: IArticle = { id: 74659 };
        mvtStk.entreprise = entreprise;

        activatedRoute.data = of({ mvtStk });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(mvtStk));
        expect(comp.articlesSharedCollection).toContain(entreprise);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<MvtStk>>();
        const mvtStk = { id: 123 };
        jest.spyOn(mvtStkService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ mvtStk });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: mvtStk }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(mvtStkService.update).toHaveBeenCalledWith(mvtStk);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<MvtStk>>();
        const mvtStk = new MvtStk();
        jest.spyOn(mvtStkService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ mvtStk });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: mvtStk }));
        saveSubject.complete();

        // THEN
        expect(mvtStkService.create).toHaveBeenCalledWith(mvtStk);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<MvtStk>>();
        const mvtStk = { id: 123 };
        jest.spyOn(mvtStkService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ mvtStk });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(mvtStkService.update).toHaveBeenCalledWith(mvtStk);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackArticleById', () => {
        it('Should return tracked Article primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackArticleById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
