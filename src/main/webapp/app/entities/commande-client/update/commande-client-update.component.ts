import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ICommandeClient, CommandeClient } from '../commande-client.model';
import { CommandeClientService } from '../service/commande-client.service';
import { IClient } from 'app/entities/client/client.model';
import { ClientService } from 'app/entities/client/service/client.service';

@Component({
  selector: 'jhi-commande-client-update',
  templateUrl: './commande-client-update.component.html',
})
export class CommandeClientUpdateComponent implements OnInit {
  isSaving = false;

  clientsSharedCollection: IClient[] = [];

  editForm = this.fb.group({
    id: [],
    code: [],
    dateCommande: [],
    client: [],
  });

  constructor(
    protected commandeClientService: CommandeClientService,
    protected clientService: ClientService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ commandeClient }) => {
      if (commandeClient.id === undefined) {
        const today = dayjs().startOf('day');
        commandeClient.dateCommande = today;
      }

      this.updateForm(commandeClient);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const commandeClient = this.createFromForm();
    if (commandeClient.id !== undefined) {
      this.subscribeToSaveResponse(this.commandeClientService.update(commandeClient));
    } else {
      this.subscribeToSaveResponse(this.commandeClientService.create(commandeClient));
    }
  }

  trackClientById(index: number, item: IClient): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICommandeClient>>): void {
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

  protected updateForm(commandeClient: ICommandeClient): void {
    this.editForm.patchValue({
      id: commandeClient.id,
      code: commandeClient.code,
      dateCommande: commandeClient.dateCommande ? commandeClient.dateCommande.format(DATE_TIME_FORMAT) : null,
      client: commandeClient.client,
    });

    this.clientsSharedCollection = this.clientService.addClientToCollectionIfMissing(this.clientsSharedCollection, commandeClient.client);
  }

  protected loadRelationshipsOptions(): void {
    this.clientService
      .query()
      .pipe(map((res: HttpResponse<IClient[]>) => res.body ?? []))
      .pipe(map((clients: IClient[]) => this.clientService.addClientToCollectionIfMissing(clients, this.editForm.get('client')!.value)))
      .subscribe((clients: IClient[]) => (this.clientsSharedCollection = clients));
  }

  protected createFromForm(): ICommandeClient {
    return {
      ...new CommandeClient(),
      id: this.editForm.get(['id'])!.value,
      code: this.editForm.get(['code'])!.value,
      dateCommande: this.editForm.get(['dateCommande'])!.value
        ? dayjs(this.editForm.get(['dateCommande'])!.value, DATE_TIME_FORMAT)
        : undefined,
      client: this.editForm.get(['client'])!.value,
    };
  }
}
