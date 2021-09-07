import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IVente, Vente } from '../vente.model';
import { VenteService } from '../service/vente.service';

@Component({
  selector: 'jhi-vente-update',
  templateUrl: './vente-update.component.html',
})
export class VenteUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    code: [],
    dateVente: [],
    commentaire: [],
  });

  constructor(protected venteService: VenteService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vente }) => {
      if (vente.id === undefined) {
        const today = dayjs().startOf('day');
        vente.dateVente = today;
      }

      this.updateForm(vente);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const vente = this.createFromForm();
    if (vente.id !== undefined) {
      this.subscribeToSaveResponse(this.venteService.update(vente));
    } else {
      this.subscribeToSaveResponse(this.venteService.create(vente));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVente>>): void {
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

  protected updateForm(vente: IVente): void {
    this.editForm.patchValue({
      id: vente.id,
      code: vente.code,
      dateVente: vente.dateVente ? vente.dateVente.format(DATE_TIME_FORMAT) : null,
      commentaire: vente.commentaire,
    });
  }

  protected createFromForm(): IVente {
    return {
      ...new Vente(),
      id: this.editForm.get(['id'])!.value,
      code: this.editForm.get(['code'])!.value,
      dateVente: this.editForm.get(['dateVente'])!.value ? dayjs(this.editForm.get(['dateVente'])!.value, DATE_TIME_FORMAT) : undefined,
      commentaire: this.editForm.get(['commentaire'])!.value,
    };
  }
}
