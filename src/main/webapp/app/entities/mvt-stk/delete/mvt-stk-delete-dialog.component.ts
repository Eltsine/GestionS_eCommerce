import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IMvtStk } from '../mvt-stk.model';
import { MvtStkService } from '../service/mvt-stk.service';

@Component({
  templateUrl: './mvt-stk-delete-dialog.component.html',
})
export class MvtStkDeleteDialogComponent {
  mvtStk?: IMvtStk;

  constructor(protected mvtStkService: MvtStkService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.mvtStkService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
