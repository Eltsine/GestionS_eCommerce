jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ArticleService } from '../service/article.service';
import { IArticle, Article } from '../article.model';
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

import { ArticleUpdateComponent } from './article-update.component';

describe('Component Tests', () => {
  describe('Article Management Update Component', () => {
    let comp: ArticleUpdateComponent;
    let fixture: ComponentFixture<ArticleUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let articleService: ArticleService;
    let entrepriseService: EntrepriseService;
    let ligneCommandeClientService: LigneCommandeClientService;
    let ligneCommandeFournisseurService: LigneCommandeFournisseurService;
    let ligneVenteService: LigneVenteService;
    let categorieService: CategorieService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ArticleUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ArticleUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ArticleUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      articleService = TestBed.inject(ArticleService);
      entrepriseService = TestBed.inject(EntrepriseService);
      ligneCommandeClientService = TestBed.inject(LigneCommandeClientService);
      ligneCommandeFournisseurService = TestBed.inject(LigneCommandeFournisseurService);
      ligneVenteService = TestBed.inject(LigneVenteService);
      categorieService = TestBed.inject(CategorieService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Entreprise query and add missing value', () => {
        const article: IArticle = { id: 456 };
        const entreprise: IEntreprise = { id: 79605 };
        article.entreprise = entreprise;

        const entrepriseCollection: IEntreprise[] = [{ id: 62796 }];
        jest.spyOn(entrepriseService, 'query').mockReturnValue(of(new HttpResponse({ body: entrepriseCollection })));
        const additionalEntreprises = [entreprise];
        const expectedCollection: IEntreprise[] = [...additionalEntreprises, ...entrepriseCollection];
        jest.spyOn(entrepriseService, 'addEntrepriseToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ article });
        comp.ngOnInit();

        expect(entrepriseService.query).toHaveBeenCalled();
        expect(entrepriseService.addEntrepriseToCollectionIfMissing).toHaveBeenCalledWith(entrepriseCollection, ...additionalEntreprises);
        expect(comp.entreprisesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call LigneCommandeClient query and add missing value', () => {
        const article: IArticle = { id: 456 };
        const ligneCommandeClient: ILigneCommandeClient = { id: 72129 };
        article.ligneCommandeClient = ligneCommandeClient;

        const ligneCommandeClientCollection: ILigneCommandeClient[] = [{ id: 9 }];
        jest.spyOn(ligneCommandeClientService, 'query').mockReturnValue(of(new HttpResponse({ body: ligneCommandeClientCollection })));
        const additionalLigneCommandeClients = [ligneCommandeClient];
        const expectedCollection: ILigneCommandeClient[] = [...additionalLigneCommandeClients, ...ligneCommandeClientCollection];
        jest.spyOn(ligneCommandeClientService, 'addLigneCommandeClientToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ article });
        comp.ngOnInit();

        expect(ligneCommandeClientService.query).toHaveBeenCalled();
        expect(ligneCommandeClientService.addLigneCommandeClientToCollectionIfMissing).toHaveBeenCalledWith(
          ligneCommandeClientCollection,
          ...additionalLigneCommandeClients
        );
        expect(comp.ligneCommandeClientsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call LigneCommandeFournisseur query and add missing value', () => {
        const article: IArticle = { id: 456 };
        const ligneCommandeFournisseur: ILigneCommandeFournisseur = { id: 77425 };
        article.ligneCommandeFournisseur = ligneCommandeFournisseur;

        const ligneCommandeFournisseurCollection: ILigneCommandeFournisseur[] = [{ id: 15939 }];
        jest
          .spyOn(ligneCommandeFournisseurService, 'query')
          .mockReturnValue(of(new HttpResponse({ body: ligneCommandeFournisseurCollection })));
        const additionalLigneCommandeFournisseurs = [ligneCommandeFournisseur];
        const expectedCollection: ILigneCommandeFournisseur[] = [
          ...additionalLigneCommandeFournisseurs,
          ...ligneCommandeFournisseurCollection,
        ];
        jest.spyOn(ligneCommandeFournisseurService, 'addLigneCommandeFournisseurToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ article });
        comp.ngOnInit();

        expect(ligneCommandeFournisseurService.query).toHaveBeenCalled();
        expect(ligneCommandeFournisseurService.addLigneCommandeFournisseurToCollectionIfMissing).toHaveBeenCalledWith(
          ligneCommandeFournisseurCollection,
          ...additionalLigneCommandeFournisseurs
        );
        expect(comp.ligneCommandeFournisseursSharedCollection).toEqual(expectedCollection);
      });

      it('Should call LigneVente query and add missing value', () => {
        const article: IArticle = { id: 456 };
        const ligneVente: ILigneVente = { id: 366 };
        article.ligneVente = ligneVente;

        const ligneVenteCollection: ILigneVente[] = [{ id: 99108 }];
        jest.spyOn(ligneVenteService, 'query').mockReturnValue(of(new HttpResponse({ body: ligneVenteCollection })));
        const additionalLigneVentes = [ligneVente];
        const expectedCollection: ILigneVente[] = [...additionalLigneVentes, ...ligneVenteCollection];
        jest.spyOn(ligneVenteService, 'addLigneVenteToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ article });
        comp.ngOnInit();

        expect(ligneVenteService.query).toHaveBeenCalled();
        expect(ligneVenteService.addLigneVenteToCollectionIfMissing).toHaveBeenCalledWith(ligneVenteCollection, ...additionalLigneVentes);
        expect(comp.ligneVentesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Categorie query and add missing value', () => {
        const article: IArticle = { id: 456 };
        const categorie: ICategorie = { id: 72279 };
        article.categorie = categorie;

        const categorieCollection: ICategorie[] = [{ id: 68937 }];
        jest.spyOn(categorieService, 'query').mockReturnValue(of(new HttpResponse({ body: categorieCollection })));
        const additionalCategories = [categorie];
        const expectedCollection: ICategorie[] = [...additionalCategories, ...categorieCollection];
        jest.spyOn(categorieService, 'addCategorieToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ article });
        comp.ngOnInit();

        expect(categorieService.query).toHaveBeenCalled();
        expect(categorieService.addCategorieToCollectionIfMissing).toHaveBeenCalledWith(categorieCollection, ...additionalCategories);
        expect(comp.categoriesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const article: IArticle = { id: 456 };
        const entreprise: IEntreprise = { id: 75328 };
        article.entreprise = entreprise;
        const ligneCommandeClient: ILigneCommandeClient = { id: 10553 };
        article.ligneCommandeClient = ligneCommandeClient;
        const ligneCommandeFournisseur: ILigneCommandeFournisseur = { id: 21082 };
        article.ligneCommandeFournisseur = ligneCommandeFournisseur;
        const ligneVente: ILigneVente = { id: 13100 };
        article.ligneVente = ligneVente;
        const categorie: ICategorie = { id: 65625 };
        article.categorie = categorie;

        activatedRoute.data = of({ article });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(article));
        expect(comp.entreprisesSharedCollection).toContain(entreprise);
        expect(comp.ligneCommandeClientsSharedCollection).toContain(ligneCommandeClient);
        expect(comp.ligneCommandeFournisseursSharedCollection).toContain(ligneCommandeFournisseur);
        expect(comp.ligneVentesSharedCollection).toContain(ligneVente);
        expect(comp.categoriesSharedCollection).toContain(categorie);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Article>>();
        const article = { id: 123 };
        jest.spyOn(articleService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ article });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: article }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(articleService.update).toHaveBeenCalledWith(article);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Article>>();
        const article = new Article();
        jest.spyOn(articleService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ article });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: article }));
        saveSubject.complete();

        // THEN
        expect(articleService.create).toHaveBeenCalledWith(article);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Article>>();
        const article = { id: 123 };
        jest.spyOn(articleService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ article });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(articleService.update).toHaveBeenCalledWith(article);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackEntrepriseById', () => {
        it('Should return tracked Entreprise primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackEntrepriseById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackLigneCommandeClientById', () => {
        it('Should return tracked LigneCommandeClient primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackLigneCommandeClientById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackLigneCommandeFournisseurById', () => {
        it('Should return tracked LigneCommandeFournisseur primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackLigneCommandeFournisseurById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackLigneVenteById', () => {
        it('Should return tracked LigneVente primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackLigneVenteById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackCategorieById', () => {
        it('Should return tracked Categorie primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackCategorieById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
