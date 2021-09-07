import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IAdresse, Adresse } from '../adresse.model';
import { AdresseService } from '../service/adresse.service';

@Component({
  selector: 'jhi-adresse-update',
  templateUrl: './adresse-update.component.html',
})
export class AdresseUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    adresse1: [],
    adresse2: [],
    ville: [],
    codePostal: [],
    pays: [],
  });

  constructor(protected adresseService: AdresseService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ adresse }) => {
      this.updateForm(adresse);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const adresse = this.createFromForm();
    if (adresse.id !== undefined) {
      this.subscribeToSaveResponse(this.adresseService.update(adresse));
    } else {
      this.subscribeToSaveResponse(this.adresseService.create(adresse));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAdresse>>): void {
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

  protected updateForm(adresse: IAdresse): void {
    this.editForm.patchValue({
      id: adresse.id,
      adresse1: adresse.adresse1,
      adresse2: adresse.adresse2,
      ville: adresse.ville,
      codePostal: adresse.codePostal,
      pays: adresse.pays,
    });
  }

  protected createFromForm(): IAdresse {
    return {
      ...new Adresse(),
      id: this.editForm.get(['id'])!.value,
      adresse1: this.editForm.get(['adresse1'])!.value,
      adresse2: this.editForm.get(['adresse2'])!.value,
      ville: this.editForm.get(['ville'])!.value,
      codePostal: this.editForm.get(['codePostal'])!.value,
      pays: this.editForm.get(['pays'])!.value,
    };
  }
}
