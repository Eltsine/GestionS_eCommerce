import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IArticle, Article } from '../article.model';
import { ArticleService } from '../service/article.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IEntreprise } from 'app/entities/entreprise/entreprise.model';
import { EntrepriseService } from 'app/entities/entreprise/service/entreprise.service';
import { ILigneCommandeClient } from 'app/entities/ligne-commande-client/ligne-commande-client.model';
import { LigneCommandeClientService } from 'app/entities/ligne-commande-client/service/ligne-commande-client.service';
import { ILigneCommandeFournisseur } from 'app/entities/ligne-commande-fournisseur/ligne-commande-fournisseur.model';
import { LigneCommandeFournisseurService } from 'app/entities/ligne-commande-fournisseur/service/ligne-commande-fournisseur.service';
import { ILigneVente } from 'app/entities/ligne-vente/ligne-vente.model';
import { LigneVenteService } from 'app/entities/ligne-vente/service/ligne-vente.service';
import { ICategorie } from 'app/entities/categorie/categorie.model';
import { CategorieService } from 'app/entities/categorie/service/categorie.service';

@Component({
  selector: 'jhi-article-update',
  templateUrl: './article-update.component.html',
})
export class ArticleUpdateComponent implements OnInit {
  isSaving = false;

  entreprisesSharedCollection: IEntreprise[] = [];
  ligneCommandeClientsSharedCollection: ILigneCommandeClient[] = [];
  ligneCommandeFournisseursSharedCollection: ILigneCommandeFournisseur[] = [];
  ligneVentesSharedCollection: ILigneVente[] = [];
  categoriesSharedCollection: ICategorie[] = [];

  editForm = this.fb.group({
    id: [],
    codeArticle: [],
    designation: [],
    prixUnitaireHT: [],
    prixUnitaireTtc: [],
    tauxTva: [],
    photo: [],
    photoContentType: [],
    entreprise: [],
    ligneCommandeClient: [],
    ligneCommandeFournisseur: [],
    ligneVente: [],
    categorie: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected articleService: ArticleService,
    protected entrepriseService: EntrepriseService,
    protected ligneCommandeClientService: LigneCommandeClientService,
    protected ligneCommandeFournisseurService: LigneCommandeFournisseurService,
    protected ligneVenteService: LigneVenteService,
    protected categorieService: CategorieService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ article }) => {
      this.updateForm(article);

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
    const article = this.createFromForm();
    if (article.id !== undefined) {
      this.subscribeToSaveResponse(this.articleService.update(article));
    } else {
      this.subscribeToSaveResponse(this.articleService.create(article));
    }
  }

  trackEntrepriseById(index: number, item: IEntreprise): number {
    return item.id!;
  }

  trackLigneCommandeClientById(index: number, item: ILigneCommandeClient): number {
    return item.id!;
  }

  trackLigneCommandeFournisseurById(index: number, item: ILigneCommandeFournisseur): number {
    return item.id!;
  }

  trackLigneVenteById(index: number, item: ILigneVente): number {
    return item.id!;
  }

  trackCategorieById(index: number, item: ICategorie): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IArticle>>): void {
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

  protected updateForm(article: IArticle): void {
    this.editForm.patchValue({
      id: article.id,
      codeArticle: article.codeArticle,
      designation: article.designation,
      prixUnitaireHT: article.prixUnitaireHT,
      prixUnitaireTtc: article.prixUnitaireTtc,
      tauxTva: article.tauxTva,
      photo: article.photo,
      photoContentType: article.photoContentType,
      entreprise: article.entreprise,
      ligneCommandeClient: article.ligneCommandeClient,
      ligneCommandeFournisseur: article.ligneCommandeFournisseur,
      ligneVente: article.ligneVente,
      categorie: article.categorie,
    });

    this.entreprisesSharedCollection = this.entrepriseService.addEntrepriseToCollectionIfMissing(
      this.entreprisesSharedCollection,
      article.entreprise
    );
    this.ligneCommandeClientsSharedCollection = this.ligneCommandeClientService.addLigneCommandeClientToCollectionIfMissing(
      this.ligneCommandeClientsSharedCollection,
      article.ligneCommandeClient
    );
    this.ligneCommandeFournisseursSharedCollection = this.ligneCommandeFournisseurService.addLigneCommandeFournisseurToCollectionIfMissing(
      this.ligneCommandeFournisseursSharedCollection,
      article.ligneCommandeFournisseur
    );
    this.ligneVentesSharedCollection = this.ligneVenteService.addLigneVenteToCollectionIfMissing(
      this.ligneVentesSharedCollection,
      article.ligneVente
    );
    this.categoriesSharedCollection = this.categorieService.addCategorieToCollectionIfMissing(
      this.categoriesSharedCollection,
      article.categorie
    );
  }

  protected loadRelationshipsOptions(): void {
    this.entrepriseService
      .query()
      .pipe(map((res: HttpResponse<IEntreprise[]>) => res.body ?? []))
      .pipe(
        map((entreprises: IEntreprise[]) =>
          this.entrepriseService.addEntrepriseToCollectionIfMissing(entreprises, this.editForm.get('entreprise')!.value)
        )
      )
      .subscribe((entreprises: IEntreprise[]) => (this.entreprisesSharedCollection = entreprises));

    this.ligneCommandeClientService
      .query()
      .pipe(map((res: HttpResponse<ILigneCommandeClient[]>) => res.body ?? []))
      .pipe(
        map((ligneCommandeClients: ILigneCommandeClient[]) =>
          this.ligneCommandeClientService.addLigneCommandeClientToCollectionIfMissing(
            ligneCommandeClients,
            this.editForm.get('ligneCommandeClient')!.value
          )
        )
      )
      .subscribe((ligneCommandeClients: ILigneCommandeClient[]) => (this.ligneCommandeClientsSharedCollection = ligneCommandeClients));

    this.ligneCommandeFournisseurService
      .query()
      .pipe(map((res: HttpResponse<ILigneCommandeFournisseur[]>) => res.body ?? []))
      .pipe(
        map((ligneCommandeFournisseurs: ILigneCommandeFournisseur[]) =>
          this.ligneCommandeFournisseurService.addLigneCommandeFournisseurToCollectionIfMissing(
            ligneCommandeFournisseurs,
            this.editForm.get('ligneCommandeFournisseur')!.value
          )
        )
      )
      .subscribe(
        (ligneCommandeFournisseurs: ILigneCommandeFournisseur[]) =>
          (this.ligneCommandeFournisseursSharedCollection = ligneCommandeFournisseurs)
      );

    this.ligneVenteService
      .query()
      .pipe(map((res: HttpResponse<ILigneVente[]>) => res.body ?? []))
      .pipe(
        map((ligneVentes: ILigneVente[]) =>
          this.ligneVenteService.addLigneVenteToCollectionIfMissing(ligneVentes, this.editForm.get('ligneVente')!.value)
        )
      )
      .subscribe((ligneVentes: ILigneVente[]) => (this.ligneVentesSharedCollection = ligneVentes));

    this.categorieService
      .query()
      .pipe(map((res: HttpResponse<ICategorie[]>) => res.body ?? []))
      .pipe(
        map((categories: ICategorie[]) =>
          this.categorieService.addCategorieToCollectionIfMissing(categories, this.editForm.get('categorie')!.value)
        )
      )
      .subscribe((categories: ICategorie[]) => (this.categoriesSharedCollection = categories));
  }

  protected createFromForm(): IArticle {
    return {
      ...new Article(),
      id: this.editForm.get(['id'])!.value,
      codeArticle: this.editForm.get(['codeArticle'])!.value,
      designation: this.editForm.get(['designation'])!.value,
      prixUnitaireHT: this.editForm.get(['prixUnitaireHT'])!.value,
      prixUnitaireTtc: this.editForm.get(['prixUnitaireTtc'])!.value,
      tauxTva: this.editForm.get(['tauxTva'])!.value,
      photoContentType: this.editForm.get(['photoContentType'])!.value,
      photo: this.editForm.get(['photo'])!.value,
      entreprise: this.editForm.get(['entreprise'])!.value,
      ligneCommandeClient: this.editForm.get(['ligneCommandeClient'])!.value,
      ligneCommandeFournisseur: this.editForm.get(['ligneCommandeFournisseur'])!.value,
      ligneVente: this.editForm.get(['ligneVente'])!.value,
      categorie: this.editForm.get(['categorie'])!.value,
    };
  }
}
