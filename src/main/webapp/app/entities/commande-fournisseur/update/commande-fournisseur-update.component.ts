import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ICommandeFournisseur, CommandeFournisseur } from '../commande-fournisseur.model';
import { CommandeFournisseurService } from '../service/commande-fournisseur.service';
import { IFournisseur } from 'app/entities/fournisseur/fournisseur.model';
import { FournisseurService } from 'app/entities/fournisseur/service/fournisseur.service';

@Component({
  selector: 'jhi-commande-fournisseur-update',
  templateUrl: './commande-fournisseur-update.component.html',
})
export class CommandeFournisseurUpdateComponent implements OnInit {
  isSaving = false;

  fournisseursSharedCollection: IFournisseur[] = [];

  editForm = this.fb.group({
    id: [],
    code: [],
    dateCommande: [],
    fournisseur: [],
  });

  constructor(
    protected commandeFournisseurService: CommandeFournisseurService,
    protected fournisseurService: FournisseurService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ commandeFournisseur }) => {
      if (commandeFournisseur.id === undefined) {
        const today = dayjs().startOf('day');
        commandeFournisseur.dateCommande = today;
      }

      this.updateForm(commandeFournisseur);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const commandeFournisseur = this.createFromForm();
    if (commandeFournisseur.id !== undefined) {
      this.subscribeToSaveResponse(this.commandeFournisseurService.update(commandeFournisseur));
    } else {
      this.subscribeToSaveResponse(this.commandeFournisseurService.create(commandeFournisseur));
    }
  }

  trackFournisseurById(index: number, item: IFournisseur): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICommandeFournisseur>>): void {
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

  protected updateForm(commandeFournisseur: ICommandeFournisseur): void {
    this.editForm.patchValue({
      id: commandeFournisseur.id,
      code: commandeFournisseur.code,
      dateCommande: commandeFournisseur.dateCommande ? commandeFournisseur.dateCommande.format(DATE_TIME_FORMAT) : null,
      fournisseur: commandeFournisseur.fournisseur,
    });

    this.fournisseursSharedCollection = this.fournisseurService.addFournisseurToCollectionIfMissing(
      this.fournisseursSharedCollection,
      commandeFournisseur.fournisseur
    );
  }

  protected loadRelationshipsOptions(): void {
    this.fournisseurService
      .query()
      .pipe(map((res: HttpResponse<IFournisseur[]>) => res.body ?? []))
      .pipe(
        map((fournisseurs: IFournisseur[]) =>
          this.fournisseurService.addFournisseurToCollectionIfMissing(fournisseurs, this.editForm.get('fournisseur')!.value)
        )
      )
      .subscribe((fournisseurs: IFournisseur[]) => (this.fournisseursSharedCollection = fournisseurs));
  }

  protected createFromForm(): ICommandeFournisseur {
    return {
      ...new CommandeFournisseur(),
      id: this.editForm.get(['id'])!.value,
      code: this.editForm.get(['code'])!.value,
      dateCommande: this.editForm.get(['dateCommande'])!.value
        ? dayjs(this.editForm.get(['dateCommande'])!.value, DATE_TIME_FORMAT)
        : undefined,
      fournisseur: this.editForm.get(['fournisseur'])!.value,
    };
  }
}
