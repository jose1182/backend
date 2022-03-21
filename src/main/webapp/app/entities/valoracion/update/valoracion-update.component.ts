import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IValoracion, Valoracion } from '../valoracion.model';
import { ValoracionService } from '../service/valoracion.service';
import { IUsuario } from 'app/entities/usuario/usuario.model';
import { UsuarioService } from 'app/entities/usuario/service/usuario.service';
import { IServicio } from 'app/entities/servicio/servicio.model';
import { ServicioService } from 'app/entities/servicio/service/servicio.service';

@Component({
  selector: 'jhi-valoracion-update',
  templateUrl: './valoracion-update.component.html',
})
export class ValoracionUpdateComponent implements OnInit {
  isSaving = false;

  usuariosSharedCollection: IUsuario[] = [];
  serviciosSharedCollection: IServicio[] = [];

  editForm = this.fb.group({
    id: [],
    descripcion: [null, [Validators.required, Validators.minLength(3), Validators.maxLength(500)]],
    fecha: [null, [Validators.required]],
    puntuacion: [null, [Validators.required, Validators.min(0), Validators.max(5)]],
    usuario: [],
    servicio: [],
  });

  constructor(
    protected valoracionService: ValoracionService,
    protected usuarioService: UsuarioService,
    protected servicioService: ServicioService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ valoracion }) => {
      if (valoracion.id === undefined) {
        const today = dayjs().startOf('day');
        valoracion.fecha = today;
      }

      this.updateForm(valoracion);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const valoracion = this.createFromForm();
    if (valoracion.id !== undefined) {
      this.subscribeToSaveResponse(this.valoracionService.update(valoracion));
    } else {
      this.subscribeToSaveResponse(this.valoracionService.create(valoracion));
    }
  }

  trackUsuarioById(index: number, item: IUsuario): number {
    return item.id!;
  }

  trackServicioById(index: number, item: IServicio): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IValoracion>>): void {
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

  protected updateForm(valoracion: IValoracion): void {
    this.editForm.patchValue({
      id: valoracion.id,
      descripcion: valoracion.descripcion,
      fecha: valoracion.fecha ? valoracion.fecha.format(DATE_TIME_FORMAT) : null,
      puntuacion: valoracion.puntuacion,
      usuario: valoracion.usuario,
      servicio: valoracion.servicio,
    });

    this.usuariosSharedCollection = this.usuarioService.addUsuarioToCollectionIfMissing(this.usuariosSharedCollection, valoracion.usuario);
    this.serviciosSharedCollection = this.servicioService.addServicioToCollectionIfMissing(
      this.serviciosSharedCollection,
      valoracion.servicio
    );
  }

  protected loadRelationshipsOptions(): void {
    this.usuarioService
      .query()
      .pipe(map((res: HttpResponse<IUsuario[]>) => res.body ?? []))
      .pipe(
        map((usuarios: IUsuario[]) => this.usuarioService.addUsuarioToCollectionIfMissing(usuarios, this.editForm.get('usuario')!.value))
      )
      .subscribe((usuarios: IUsuario[]) => (this.usuariosSharedCollection = usuarios));

    this.servicioService
      .query()
      .pipe(map((res: HttpResponse<IServicio[]>) => res.body ?? []))
      .pipe(
        map((servicios: IServicio[]) =>
          this.servicioService.addServicioToCollectionIfMissing(servicios, this.editForm.get('servicio')!.value)
        )
      )
      .subscribe((servicios: IServicio[]) => (this.serviciosSharedCollection = servicios));
  }

  protected createFromForm(): IValoracion {
    return {
      ...new Valoracion(),
      id: this.editForm.get(['id'])!.value,
      descripcion: this.editForm.get(['descripcion'])!.value,
      fecha: this.editForm.get(['fecha'])!.value ? dayjs(this.editForm.get(['fecha'])!.value, DATE_TIME_FORMAT) : undefined,
      puntuacion: this.editForm.get(['puntuacion'])!.value,
      usuario: this.editForm.get(['usuario'])!.value,
      servicio: this.editForm.get(['servicio'])!.value,
    };
  }
}
