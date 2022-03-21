import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IMensaje, Mensaje } from '../mensaje.model';
import { MensajeService } from '../service/mensaje.service';
import { IUsuario } from 'app/entities/usuario/usuario.model';
import { UsuarioService } from 'app/entities/usuario/service/usuario.service';
import { IConversacion } from 'app/entities/conversacion/conversacion.model';
import { ConversacionService } from 'app/entities/conversacion/service/conversacion.service';

@Component({
  selector: 'jhi-mensaje-update',
  templateUrl: './mensaje-update.component.html',
})
export class MensajeUpdateComponent implements OnInit {
  isSaving = false;

  usuariosSharedCollection: IUsuario[] = [];
  conversacionsSharedCollection: IConversacion[] = [];

  editForm = this.fb.group({
    id: [],
    texto: [null, [Validators.required]],
    fecha: [null, [Validators.required]],
    emisor: [],
    receptor: [],
    conversacion: [],
  });

  constructor(
    protected mensajeService: MensajeService,
    protected usuarioService: UsuarioService,
    protected conversacionService: ConversacionService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ mensaje }) => {
      if (mensaje.id === undefined) {
        const today = dayjs().startOf('day');
        mensaje.fecha = today;
      }

      this.updateForm(mensaje);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const mensaje = this.createFromForm();
    if (mensaje.id !== undefined) {
      this.subscribeToSaveResponse(this.mensajeService.update(mensaje));
    } else {
      this.subscribeToSaveResponse(this.mensajeService.create(mensaje));
    }
  }

  trackUsuarioById(index: number, item: IUsuario): number {
    return item.id!;
  }

  trackConversacionById(index: number, item: IConversacion): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMensaje>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(mensaje: IMensaje): void {
    this.editForm.patchValue({
      id: mensaje.id,
      texto: mensaje.texto,
      fecha: mensaje.fecha ? mensaje.fecha.format(DATE_TIME_FORMAT) : null,
      emisor: mensaje.emisor,
      receptor: mensaje.receptor,
      conversacion: mensaje.conversacion,
    });

    this.usuariosSharedCollection = this.usuarioService.addUsuarioToCollectionIfMissing(
      this.usuariosSharedCollection,
      mensaje.emisor,
      mensaje.receptor
    );
    this.conversacionsSharedCollection = this.conversacionService.addConversacionToCollectionIfMissing(
      this.conversacionsSharedCollection,
      mensaje.conversacion
    );
  }

  protected loadRelationshipsOptions(): void {
    this.usuarioService
      .query()
      .pipe(map((res: HttpResponse<IUsuario[]>) => res.body ?? []))
      .pipe(
        map((usuarios: IUsuario[]) =>
          this.usuarioService.addUsuarioToCollectionIfMissing(
            usuarios,
            this.editForm.get('emisor')!.value,
            this.editForm.get('receptor')!.value
          )
        )
      )
      .subscribe((usuarios: IUsuario[]) => (this.usuariosSharedCollection = usuarios));

    this.conversacionService
      .query()
      .pipe(map((res: HttpResponse<IConversacion[]>) => res.body ?? []))
      .pipe(
        map((conversacions: IConversacion[]) =>
          this.conversacionService.addConversacionToCollectionIfMissing(conversacions, this.editForm.get('conversacion')!.value)
        )
      )
      .subscribe((conversacions: IConversacion[]) => (this.conversacionsSharedCollection = conversacions));
  }

  protected createFromForm(): IMensaje {
    return {
      ...new Mensaje(),
      id: this.editForm.get(['id'])!.value,
      texto: this.editForm.get(['texto'])!.value,
      fecha: this.editForm.get(['fecha'])!.value ? dayjs(this.editForm.get(['fecha'])!.value, DATE_TIME_FORMAT) : undefined,
      emisor: this.editForm.get(['emisor'])!.value,
      receptor: this.editForm.get(['receptor'])!.value,
      conversacion: this.editForm.get(['conversacion'])!.value,
    };
  }
}
