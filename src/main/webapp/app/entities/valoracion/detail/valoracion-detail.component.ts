import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IValoracion } from '../valoracion.model';

@Component({
  selector: 'jhi-valoracion-detail',
  templateUrl: './valoracion-detail.component.html',
})
export class ValoracionDetailComponent implements OnInit {
  valoracion: IValoracion | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ valoracion }) => {
      this.valoracion = valoracion;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
