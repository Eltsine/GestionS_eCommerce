import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ILigneCommandeFournisseur, LigneCommandeFournisseur } from '../ligne-commande-fournisseur.model';
import { LigneCommandeFournisseurService } from '../service/ligne-commande-fournisseur.service';
import { ICommandeFournisseur } from 'app/entities/commande-fournisseur/commande-fournisseur.model';
import { CommandeFournisseurService } from 'app/entities/commande-fournisseur/service/commande-fournisseur.service';

@Component({
  selector: 'jhi-ligne-commande-fournisseur-update',
  templateUrl: './ligne-commande-fournisseur-update.component.html',
})
export class LigneCommandeFournisseurUpdateComponent implements OnInit {
  isSaving = false;

  commandeFournisseursSharedCollection: ICommandeFournisseur[] = [];

  editForm = this.fb.group({
    id: [],
    quantite: [],
    prixUnitaire: [],
    commandeFournisseur: [],
  });

  constructor(
    protected ligneCommandeFournisseurService: LigneCommandeFournisseurService,
    protected commandeFournisseurService: CommandeFournisseurService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ligneCommandeFournisseur }) => {
      this.updateForm(ligneCommandeFournisseur);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const ligneCommandeFournisseur = this.createFromForm();
    if (ligneCommandeFournisseur.id !== undefined) {
      this.subscribeToSaveResponse(this.ligneCommandeFournisseurService.update(ligneCommandeFournisseur));
    } else {
      this.subscribeToSaveResponse(this.ligneCommandeFournisseurService.create(ligneCommandeFournisseur));
    }
  }

  trackCommandeFournisseurById(index: number, item: ICommandeFournisseur): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILigneCommandeFournisseur>>): void {
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

  protected updateForm(ligneCommandeFournisseur: ILigneCommandeFournisseur): void {
    this.editForm.patchValue({
      id: ligneCommandeFournisseur.id,
      quantite: ligneCommandeFournisseur.quantite,
      prixUnitaire: ligneCommandeFournisseur.prixUnitaire,
      commandeFournisseur: ligneCommandeFournisseur.commandeFournisseur,
    });

    this.commandeFournisseursSharedCollection = this.commandeFournisseurService.addCommandeFournisseurToCollectionIfMissing(
      this.commandeFournisseursSharedCollection,
      ligneCommandeFournisseur.commandeFournisseur
    );
  }

  protected loadRelationshipsOptions(): void {
    this.commandeFournisseurService
      .query()
      .pipe(map((res: HttpResponse<ICommandeFournisseur[]>) => res.body ?? []))
      .pipe(
        map((commandeFournisseurs: ICommandeFournisseur[]) =>
          this.commandeFournisseurService.addCommandeFournisseurToCollectionIfMissing(
            commandeFournisseurs,
            this.editForm.get('commandeFournisseur')!.value
          )
        )
      )
      .subscribe((commandeFournisseurs: ICommandeFournisseur[]) => (this.commandeFournisseursSharedCollection = commandeFournisseurs));
  }

  protected createFromForm(): ILigneCommandeFournisseur {
    return {
      ...new LigneCommandeFournisseur(),
      id: this.editForm.get(['id'])!.value,
      quantite: this.editForm.get(['quantite'])!.value,
      prixUnitaire: this.editForm.get(['prixUnitaire'])!.value,
      commandeFournisseur: this.editForm.get(['commandeFournisseur'])!.value,
    };
  }
}
