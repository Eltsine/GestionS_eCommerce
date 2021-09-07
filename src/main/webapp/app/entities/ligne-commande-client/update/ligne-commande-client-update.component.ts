import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ILigneCommandeClient, LigneCommandeClient } from '../ligne-commande-client.model';
import { LigneCommandeClientService } from '../service/ligne-commande-client.service';
import { ICommandeClient } from 'app/entities/commande-client/commande-client.model';
import { CommandeClientService } from 'app/entities/commande-client/service/commande-client.service';

@Component({
  selector: 'jhi-ligne-commande-client-update',
  templateUrl: './ligne-commande-client-update.component.html',
})
export class LigneCommandeClientUpdateComponent implements OnInit {
  isSaving = false;

  commandeClientsSharedCollection: ICommandeClient[] = [];

  editForm = this.fb.group({
    id: [],
    quantite: [],
    prixUnitaire: [],
    commandeClient: [],
  });

  constructor(
    protected ligneCommandeClientService: LigneCommandeClientService,
    protected commandeClientService: CommandeClientService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ligneCommandeClient }) => {
      this.updateForm(ligneCommandeClient);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const ligneCommandeClient = this.createFromForm();
    if (ligneCommandeClient.id !== undefined) {
      this.subscribeToSaveResponse(this.ligneCommandeClientService.update(ligneCommandeClient));
    } else {
      this.subscribeToSaveResponse(this.ligneCommandeClientService.create(ligneCommandeClient));
    }
  }

  trackCommandeClientById(index: number, item: ICommandeClient): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILigneCommandeClient>>): void {
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

  protected updateForm(ligneCommandeClient: ILigneCommandeClient): void {
    this.editForm.patchValue({
      id: ligneCommandeClient.id,
      quantite: ligneCommandeClient.quantite,
      prixUnitaire: ligneCommandeClient.prixUnitaire,
      commandeClient: ligneCommandeClient.commandeClient,
    });

    this.commandeClientsSharedCollection = this.commandeClientService.addCommandeClientToCollectionIfMissing(
      this.commandeClientsSharedCollection,
      ligneCommandeClient.commandeClient
    );
  }

  protected loadRelationshipsOptions(): void {
    this.commandeClientService
      .query()
      .pipe(map((res: HttpResponse<ICommandeClient[]>) => res.body ?? []))
      .pipe(
        map((commandeClients: ICommandeClient[]) =>
          this.commandeClientService.addCommandeClientToCollectionIfMissing(commandeClients, this.editForm.get('commandeClient')!.value)
        )
      )
      .subscribe((commandeClients: ICommandeClient[]) => (this.commandeClientsSharedCollection = commandeClients));
  }

  protected createFromForm(): ILigneCommandeClient {
    return {
      ...new LigneCommandeClient(),
      id: this.editForm.get(['id'])!.value,
      quantite: this.editForm.get(['quantite'])!.value,
      prixUnitaire: this.editForm.get(['prixUnitaire'])!.value,
      commandeClient: this.editForm.get(['commandeClient'])!.value,
    };
  }
}
