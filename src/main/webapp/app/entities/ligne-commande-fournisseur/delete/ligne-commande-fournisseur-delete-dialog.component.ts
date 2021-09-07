import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILigneCommandeFournisseur } from '../ligne-commande-fournisseur.model';
import { LigneCommandeFournisseurService } from '../service/ligne-commande-fournisseur.service';

@Component({
  templateUrl: './ligne-commande-fournisseur-delete-dialog.component.html',
})
export class LigneCommandeFournisseurDeleteDialogComponent {
  ligneCommandeFournisseur?: ILigneCommandeFournisseur;

  constructor(protected ligneCommandeFournisseurService: LigneCommandeFournisseurService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.ligneCommandeFournisseurService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
