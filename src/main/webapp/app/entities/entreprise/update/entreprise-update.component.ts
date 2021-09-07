import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IEntreprise, Entreprise } from '../entreprise.model';
import { EntrepriseService } from '../service/entreprise.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IAdresse } from 'app/entities/adresse/adresse.model';
import { AdresseService } from 'app/entities/adresse/service/adresse.service';

@Component({
  selector: 'jhi-entreprise-update',
  templateUrl: './entreprise-update.component.html',
})
export class EntrepriseUpdateComponent implements OnInit {
  isSaving = false;

  adressesSharedCollection: IAdresse[] = [];

  editForm = this.fb.group({
    id: [],
    nom: [],
    description: [],
    codeFiscal: [],
    photo: [],
    photoContentType: [],
    email: [],
    numTe: [],
    siteWeb: [],
    adresse: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected entrepriseService: EntrepriseService,
    protected adresseService: AdresseService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ entreprise }) => {
      this.updateForm(entreprise);

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('gestionSApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  clearInputImage(field: string, fieldContentType: string, idInput: string): void {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null,
    });
    if (idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const entreprise = this.createFromForm();
    if (entreprise.id !== undefined) {
      this.subscribeToSaveResponse(this.entrepriseService.update(entreprise));
    } else {
      this.subscribeToSaveResponse(this.entrepriseService.create(entreprise));
    }
  }

  trackAdresseById(index: number, item: IAdresse): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEntreprise>>): void {
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

  protected updateForm(entreprise: IEntreprise): void {
    this.editForm.patchValue({
      id: entreprise.id,
      nom: entreprise.nom,
      description: entreprise.description,
      codeFiscal: entreprise.codeFiscal,
      photo: entreprise.photo,
      photoContentType: entreprise.photoContentType,
      email: entreprise.email,
      numTe: entreprise.numTe,
      siteWeb: entreprise.siteWeb,
      adresse: entreprise.adresse,
    });

    this.adressesSharedCollection = this.adresseService.addAdresseToCollectionIfMissing(this.adressesSharedCollection, entreprise.adresse);
  }

  protected loadRelationshipsOptions(): void {
    this.adresseService
      .query()
      .pipe(map((res: HttpResponse<IAdresse[]>) => res.body ?? []))
      .pipe(
        map((adresses: IAdresse[]) => this.adresseService.addAdresseToCollectionIfMissing(adresses, this.editForm.get('adresse')!.value))
      )
      .subscribe((adresses: IAdresse[]) => (this.adressesSharedCollection = adresses));
  }

  protected createFromForm(): IEntreprise {
    return {
      ...new Entreprise(),
      id: this.editForm.get(['id'])!.value,
      nom: this.editForm.get(['nom'])!.value,
      description: this.editForm.get(['description'])!.value,
      codeFiscal: this.editForm.get(['codeFiscal'])!.value,
      photoContentType: this.editForm.get(['photoContentType'])!.value,
      photo: this.editForm.get(['photo'])!.value,
      email: this.editForm.get(['email'])!.value,
      numTe: this.editForm.get(['numTe'])!.value,
      siteWeb: this.editForm.get(['siteWeb'])!.value,
      adresse: this.editForm.get(['adresse'])!.value,
    };
  }
}
