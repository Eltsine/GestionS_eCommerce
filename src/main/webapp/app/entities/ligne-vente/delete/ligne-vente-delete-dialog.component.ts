import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILigneVente } from '../ligne-vente.model';
import { LigneVenteService } from '../service/ligne-vente.service';

@Component({
  templateUrl: './ligne-vente-delete-dialog.component.html',
})
export class LigneVenteDeleteDialogComponent {
  ligneVente?: ILigneVente;

  constructor(protected ligneVenteService: LigneVenteService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.ligneVenteService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
