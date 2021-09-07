import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'personnel',
        data: { pageTitle: 'gestionSApp.personnel.home.title' },
        loadChildren: () => import('./personnel/personnel.module').then(m => m.PersonnelModule),
      },
      {
        path: 'entreprise',
        data: { pageTitle: 'gestionSApp.entreprise.home.title' },
        loadChildren: () => import('./entreprise/entreprise.module').then(m => m.EntrepriseModule),
      },
      {
        path: 'article',
        data: { pageTitle: 'gestionSApp.article.home.title' },
        loadChildren: () => import('./article/article.module').then(m => m.ArticleModule),
      },
      {
        path: 'categorie',
        data: { pageTitle: 'gestionSApp.categorie.home.title' },
        loadChildren: () => import('./categorie/categorie.module').then(m => m.CategorieModule),
      },
      {
        path: 'mvt-stk',
        data: { pageTitle: 'gestionSApp.mvtStk.home.title' },
        loadChildren: () => import('./mvt-stk/mvt-stk.module').then(m => m.MvtStkModule),
      },
      {
        path: 'commande-fournisseur',
        data: { pageTitle: 'gestionSApp.commandeFournisseur.home.title' },
        loadChildren: () => import('./commande-fournisseur/commande-fournisseur.module').then(m => m.CommandeFournisseurModule),
      },
      {
        path: 'fournisseur',
        data: { pageTitle: 'gestionSApp.fournisseur.home.title' },
        loadChildren: () => import('./fournisseur/fournisseur.module').then(m => m.FournisseurModule),
      },
      {
        path: 'ligne-commande-client',
        data: { pageTitle: 'gestionSApp.ligneCommandeClient.home.title' },
        loadChildren: () => import('./ligne-commande-client/ligne-commande-client.module').then(m => m.LigneCommandeClientModule),
      },
      {
        path: 'ligne-commande-fournisseur',
        data: { pageTitle: 'gestionSApp.ligneCommandeFournisseur.home.title' },
        loadChildren: () =>
          import('./ligne-commande-fournisseur/ligne-commande-fournisseur.module').then(m => m.LigneCommandeFournisseurModule),
      },
      {
        path: 'commande-client',
        data: { pageTitle: 'gestionSApp.commandeClient.home.title' },
        loadChildren: () => import('./commande-client/commande-client.module').then(m => m.CommandeClientModule),
      },
      {
        path: 'client',
        data: { pageTitle: 'gestionSApp.client.home.title' },
        loadChildren: () => import('./client/client.module').then(m => m.ClientModule),
      },
      {
        path: 'ligne-vente',
        data: { pageTitle: 'gestionSApp.ligneVente.home.title' },
        loadChildren: () => import('./ligne-vente/ligne-vente.module').then(m => m.LigneVenteModule),
      },
      {
        path: 'vente',
        data: { pageTitle: 'gestionSApp.vente.home.title' },
        loadChildren: () => import('./vente/vente.module').then(m => m.VenteModule),
      },
      {
        path: 'adresse',
        data: { pageTitle: 'gestionSApp.adresse.home.title' },
        loadChildren: () => import('./adresse/adresse.module').then(m => m.AdresseModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
