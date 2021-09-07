import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILigneCommandeClient } from '../ligne-commande-client.model';

@Component({
  selector: 'jhi-ligne-commande-client-detail',
  templateUrl: './ligne-commande-client-detail.component.html',
})
export class LigneCommandeClientDetailComponent implements OnInit {
  ligneCommandeClient: ILigneCommandeClient | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ligneCommandeClient }) => {
      this.ligneCommandeClient = ligneCommandeClient;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
