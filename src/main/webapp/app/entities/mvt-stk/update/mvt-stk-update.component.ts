import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IMvtStk, MvtStk } from '../mvt-stk.model';
import { MvtStkService } from '../service/mvt-stk.service';
import { IArticle } from 'app/entities/article/article.model';
import { ArticleService } from 'app/entities/article/service/article.service';

@Component({
  selector: 'jhi-mvt-stk-update',
  templateUrl: './mvt-stk-update.component.html',
})
export class MvtStkUpdateComponent implements OnInit {
  isSaving = false;

  articlesSharedCollection: IArticle[] = [];

  editForm = this.fb.group({
    id: [],
    dateMvt: [],
    quantite: [],
    typeMvtStk: [],
    entreprise: [],
  });

  constructor(
    protected mvtStkService: MvtStkService,
    protected articleService: ArticleService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ mvtStk }) => {
      if (mvtStk.id === undefined) {
        const today = dayjs().startOf('day');
        mvtStk.dateMvt = today;
      }

      this.updateForm(mvtStk);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const mvtStk = this.createFromForm();
    if (mvtStk.id !== undefined) {
      this.subscribeToSaveResponse(this.mvtStkService.update(mvtStk));
    } else {
      this.subscribeToSaveResponse(this.mvtStkService.create(mvtStk));
    }
  }

  trackArticleById(index: number, item: IArticle): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMvtStk>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(mvtStk: IMvtStk): void {
    this.editForm.patchValue({
      id: mvtStk.id,
      dateMvt: mvtStk.dateMvt ? mvtStk.dateMvt.format(DATE_TIME_FORMAT) : null,
      quantite: mvtStk.quantite,
      typeMvtStk: mvtStk.typeMvtStk,
      entreprise: mvtStk.entreprise,
    });

    this.articlesSharedCollection = this.articleService.addArticleToCollectionIfMissing(this.articlesSharedCollection, mvtStk.entreprise);
  }

  protected loadRelationshipsOptions(): void {
    this.articleService
      .query()
      .pipe(map((res: HttpResponse<IArticle[]>) => res.body ?? []))
      .pipe(
        map((articles: IArticle[]) => this.articleService.addArticleToCollectionIfMissing(articles, this.editForm.get('entreprise')!.value))
      )
      .subscribe((articles: IArticle[]) => (this.articlesSharedCollection = articles));
  }

  protected createFromForm(): IMvtStk {
    return {
      ...new MvtStk(),
      id: this.editForm.get(['id'])!.value,
      dateMvt: this.editForm.get(['dateMvt'])!.value ? dayjs(this.editForm.get(['dateMvt'])!.value, DATE_TIME_FORMAT) : undefined,
      quantite: this.editForm.get(['quantite'])!.value,
      typeMvtStk: this.editForm.get(['typeMvtStk'])!.value,
      entreprise: this.editForm.get(['entreprise'])!.value,
    };
  }
}
