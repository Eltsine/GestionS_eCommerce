import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IPersonnel, Personnel } from '../personnel.model';
import { PersonnelService } from '../service/personnel.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IAdresse } from 'app/entities/adresse/adresse.model';
import { AdresseService } from 'app/entities/adresse/service/adresse.service';
import { IEntreprise } from 'app/entities/entreprise/entreprise.model';
import { EntrepriseService } from 'app/entities/entreprise/service/entreprise.service';

@Component({
  selector: 'jhi-personnel-update',
  templateUrl: './personnel-update.component.html',
})
export class PersonnelUpdateComponent implements OnInit {
  isSaving = false;

  adressesSharedCollection: IAdresse[] = [];
  entreprisesSharedCollection: IEntreprise[] = [];

  editForm = this.fb.group({
    id: [],
    nom: [],
    prenom: [],
    dateNaissance: [],
    email: [],
    motDePass: [],
    photo: [],
    photoContentType: [],
    adresse: [],
    entreprise: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected personnelService: PersonnelService,
    protected adresseService: AdresseService,
    protected entrepriseService: EntrepriseService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ personnel }) => {
      if (personnel.id === undefined) {
        const today = dayjs().startOf('day');
        personnel.dateNaissance = today;
      }

      this.updateForm(personnel);

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
    const personnel = this.createFromForm();
    if (personnel.id !== undefined) {
      this.subscribeToSaveResponse(this.personnelService.update(personnel));
    } else {
      this.subscribeToSaveResponse(this.personnelService.create(personnel));
    }
  }

  trackAdresseById(index: number, item: IAdresse): number {
    return item.id!;
  }

  trackEntrepriseById(index: number, item: IEntreprise): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPersonnel>>): void {
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

  protected updateForm(personnel: IPersonnel): void {
    this.editForm.patchValue({
      id: personnel.id,
      nom: personnel.nom,
      prenom: personnel.prenom,
      dateNaissance: personnel.dateNaissance ? personnel.dateNaissance.format(DATE_TIME_FORMAT) : null,
      email: personnel.email,
      motDePass: personnel.motDePass,
      photo: personnel.photo,
      photoContentType: personnel.photoContentType,
      adresse: personnel.adresse,
      entreprise: personnel.entreprise,
    });

    this.adressesSharedCollection = this.adresseService.addAdresseToCollectionIfMissing(this.adressesSharedCollection, personnel.adresse);
    this.entreprisesSharedCollection = this.entrepriseService.addEntrepriseToCollectionIfMissing(
      this.entreprisesSharedCollection,
      personnel.entreprise
    );
  }

  protected loadRelationshipsOptions(): void {
    this.adresseService
      .query()
      .pipe(map((res: HttpResponse<IAdresse[]>) => res.body ?? []))
      .pipe(
        map((adresses: IAdresse[]) => this.adresseService.addAdresseToCollectionIfMissing(adresses, this.editForm.get('adresse')!.value))
      )
      .subscribe((adresses: IAdresse[]) => (this.adressesSharedCollection = adresses));

    this.entrepriseService
      .query()
      .pipe(map((res: HttpResponse<IEntreprise[]>) => res.body ?? []))
      .pipe(
        map((entreprises: IEntreprise[]) =>
          this.entrepriseService.addEntrepriseToCollectionIfMissing(entreprises, this.editForm.get('entreprise')!.value)
        )
      )
      .subscribe((entreprises: IEntreprise[]) => (this.entreprisesSharedCollection = entreprises));
  }

  protected createFromForm(): IPersonnel {
    return {
      ...new Personnel(),
      id: this.editForm.get(['id'])!.value,
      nom: this.editForm.get(['nom'])!.value,
      prenom: this.editForm.get(['prenom'])!.value,
      dateNaissance: this.editForm.get(['dateNaissance'])!.value
        ? dayjs(this.editForm.get(['dateNaissance'])!.value, DATE_TIME_FORMAT)
        : undefined,
      email: this.editForm.get(['email'])!.value,
      motDePass: this.editForm.get(['motDePass'])!.value,
      photoContentType: this.editForm.get(['photoContentType'])!.value,
      photo: this.editForm.get(['photo'])!.value,
      adresse: this.editForm.get(['adresse'])!.value,
      entreprise: this.editForm.get(['entreprise'])!.value,
    };
  }
}
