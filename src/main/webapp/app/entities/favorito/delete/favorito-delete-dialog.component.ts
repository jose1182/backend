import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFavorito } from '../favorito.model';
import { FavoritoService } from '../service/favorito.service';

@Component({
  templateUrl: './favorito-delete-dialog.component.html',
})
export class FavoritoDeleteDialogComponent {
  favorito?: IFavorito;

  constructor(protected favoritoService: FavoritoService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.favoritoService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
