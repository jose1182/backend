import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IValoracion } from '../valoracion.model';
import { ValoracionService } from '../service/valoracion.service';

@Component({
  templateUrl: './valoracion-delete-dialog.component.html',
})
export class ValoracionDeleteDialogComponent {
  valoracion?: IValoracion;

  constructor(protected valoracionService: ValoracionService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.valoracionService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
