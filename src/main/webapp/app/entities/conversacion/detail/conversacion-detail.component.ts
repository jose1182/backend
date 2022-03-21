import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IConversacion } from '../conversacion.model';

@Component({
  selector: 'jhi-conversacion-detail',
  templateUrl: './conversacion-detail.component.html',
})
export class ConversacionDetailComponent implements OnInit {
  conversacion: IConversacion | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ conversacion }) => {
      this.conversacion = conversacion;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
