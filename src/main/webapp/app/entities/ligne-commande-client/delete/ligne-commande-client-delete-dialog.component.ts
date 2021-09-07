import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILigneCommandeClient } from '../ligne-commande-client.model';
import { LigneCommandeClientService } from '../service/ligne-commande-client.service';

@Component({
  templateUrl: './ligne-commande-client-delete-dialog.component.html',
})
export class LigneCommandeClientDeleteDialogComponent {
  ligneCommandeClient?: ILigneCommandeClient;

  constructor(protected ligneCommandeClientService: LigneCommandeClientService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.ligneCommandeClientService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
