import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFavorito } from '../favorito.model';

@Component({
  selector: 'jhi-favorito-detail',
  templateUrl: './favorito-detail.component.html',
})
export class FavoritoDetailComponent implements OnInit {
  favorito: IFavorito | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ favorito }) => {
      this.favorito = favorito;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
