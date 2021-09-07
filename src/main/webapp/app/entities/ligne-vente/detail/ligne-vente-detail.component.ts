import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILigneVente } from '../ligne-vente.model';

@Component({
  selector: 'jhi-ligne-vente-detail',
  templateUrl: './ligne-vente-detail.component.html',
})
export class LigneVenteDetailComponent implements OnInit {
  ligneVente: ILigneVente | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ligneVente }) => {
      this.ligneVente = ligneVente;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
