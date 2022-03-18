import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IValoracion } from '../valoracion.model';
import { ValoracionService } from '../service/valoracion.service';
import { ValoracionDeleteDialogComponent } from '../delete/valoracion-delete-dialog.component';

@Component({
  selector: 'jhi-valoracion',
  templateUrl: './valoracion.component.html',
})
export class ValoracionComponent implements OnInit {
  valoracions?: IValoracion[];
  isLoading = false;

  constructor(protected valoracionService: ValoracionService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.valoracionService.query().subscribe({
      next: (res: HttpResponse<IValoracion[]>) => {
        this.isLoading = false;
        this.valoracions = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IValoracion): number {
    return item.id!;
  }

  delete(valoracion: IValoracion): void {
    const modalRef = this.modalService.open(ValoracionDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.valoracion = valoracion;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
