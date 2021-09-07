import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ILigneVente, LigneVente } from '../ligne-vente.model';
import { LigneVenteService } from '../service/ligne-vente.service';
import { IVente } from 'app/entities/vente/vente.model';
import { VenteService } from 'app/entities/vente/service/vente.service';

@Component({
  selector: 'jhi-ligne-vente-update',
  templateUrl: './ligne-vente-update.component.html',
})
export class LigneVenteUpdateComponent implements OnInit {
  isSaving = false;

  ventesSharedCollection: IVente[] = [];

  editForm = this.fb.group({
    id: [],
    quantite: [],
    prixUnitaire: [],
    vente: [],
  });

  constructor(
    protected ligneVenteService: LigneVenteService,
    protected venteService: VenteService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ligneVente }) => {
      this.updateForm(ligneVente);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const ligneVente = this.createFromForm();
    if (ligneVente.id !== undefined) {
      this.subscribeToSaveResponse(this.ligneVenteService.update(ligneVente));
    } else {
      this.subscribeToSaveResponse(this.ligneVenteService.create(ligneVente));
    }
  }

  trackVenteById(index: number, item: IVente): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILigneVente>>): void {
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

  protected updateForm(ligneVente: ILigneVente): void {
    this.editForm.patchValue({
      id: ligneVente.id,
      quantite: ligneVente.quantite,
      prixUnitaire: ligneVente.prixUnitaire,
      vente: ligneVente.vente,
    });

    this.ventesSharedCollection = this.venteService.addVenteToCollectionIfMissing(this.ventesSharedCollection, ligneVente.vente);
  }

  protected loadRelationshipsOptions(): void {
    this.venteService
      .query()
      .pipe(map((res: HttpResponse<IVente[]>) => res.body ?? []))
      .pipe(map((ventes: IVente[]) => this.venteService.addVenteToCollectionIfMissing(ventes, this.editForm.get('vente')!.value)))
      .subscribe((ventes: IVente[]) => (this.ventesSharedCollection = ventes));
  }

  protected createFromForm(): ILigneVente {
    return {
      ...new LigneVente(),
      id: this.editForm.get(['id'])!.value,
      quantite: this.editForm.get(['quantite'])!.value,
      prixUnitaire: this.editForm.get(['prixUnitaire'])!.value,
      vente: this.editForm.get(['vente'])!.value,
    };
  }
}
