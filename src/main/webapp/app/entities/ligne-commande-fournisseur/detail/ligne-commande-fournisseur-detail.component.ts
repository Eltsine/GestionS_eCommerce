import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILigneCommandeFournisseur } from '../ligne-commande-fournisseur.model';

@Component({
  selector: 'jhi-ligne-commande-fournisseur-detail',
  templateUrl: './ligne-commande-fournisseur-detail.component.html',
})
export class LigneCommandeFournisseurDetailComponent implements OnInit {
  ligneCommandeFournisseur: ILigneCommandeFournisseur | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ligneCommandeFournisseur }) => {
      this.ligneCommandeFournisseur = ligneCommandeFournisseur;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
