import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IServicio, Servicio } from '../servicio.model';
import { ServicioService } from '../service/servicio.service';
import { IUsuario } from 'app/entities/usuario/usuario.model';
import { UsuarioService } from 'app/entities/usuario/service/usuario.service';
import { ICategoria } from 'app/entities/categoria/categoria.model';
import { CategoriaService } from 'app/entities/categoria/service/categoria.service';
import { Disponibilidad } from 'app/entities/enumerations/disponibilidad.model';

@Component({
  selector: 'jhi-servicio-update',
  templateUrl: './servicio-update.component.html',
})
export class ServicioUpdateComponent implements OnInit {
  isSaving = false;
  disponibilidadValues = Object.keys(Disponibilidad);

  usuariosSharedCollection: IUsuario[] = [];
  categoriasSharedCollection: ICategoria[] = [];

  editForm = this.fb.group({
    id: [],
    titulo: [null, [Validators.required, Validators.maxLength(60)]],
    descripcion: [null, [Validators.required]],
    disponibilidad: [null, [Validators.required]],
    preciohora: [null, [Validators.required, Validators.min(0)]],
    preciotraslado: [null, [Validators.required, Validators.min(0)]],
    fechacreacion: [null, [Validators.required]],
    fechaactualizacion: [null, [Validators.required]],
    destacado: [null, [Validators.required]],
    usuario: [],
    categorias: [],
  });

  constructor(
    protected servicioService: ServicioService,
    protected usuarioService: UsuarioService,
    protected categoriaService: CategoriaService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ servicio }) => {
      if (servicio.id === undefined) {
        const today = dayjs().startOf('day');
        servicio.fechacreacion = today;
        servicio.fechaactualizacion = today;
      }

      this.updateForm(servicio);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const servicio = this.createFromForm();
    if (servicio.id !== undefined) {
      this.subscribeToSaveResponse(this.servicioService.update(servicio));
    } else {
      this.subscribeToSaveResponse(this.servicioService.create(servicio));
    }
  }

  trackUsuarioById(index: number, item: IUsuario): number {
    return item.id!;
  }

  trackCategoriaById(index: number, item: ICategoria): number {
    return item.id!;
  }

  getSelectedCategoria(option: ICategoria, selectedVals?: ICategoria[]): ICategoria {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IServicio>>): void {
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

  protected updateForm(servicio: IServicio): void {
    this.editForm.patchValue({
      id: servicio.id,
      titulo: servicio.titulo,
      descripcion: servicio.descripcion,
      disponibilidad: servicio.disponibilidad,
      preciohora: servicio.preciohora,
      preciotraslado: servicio.preciotraslado,
      fechacreacion: servicio.fechacreacion ? servicio.fechacreacion.format(DATE_TIME_FORMAT) : null,
      fechaactualizacion: servicio.fechaactualizacion ? servicio.fechaactualizacion.format(DATE_TIME_FORMAT) : null,
      destacado: servicio.destacado,
      usuario: servicio.usuario,
      categorias: servicio.categorias,
    });

    this.usuariosSharedCollection = this.usuarioService.addUsuarioToCollectionIfMissing(this.usuariosSharedCollection, servicio.usuario);
    this.categoriasSharedCollection = this.categoriaService.addCategoriaToCollectionIfMissing(
      this.categoriasSharedCollection,
      ...(servicio.categorias ?? [])
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

    this.categoriaService
      .query()
      .pipe(map((res: HttpResponse<ICategoria[]>) => res.body ?? []))
      .pipe(
        map((categorias: ICategoria[]) =>
          this.categoriaService.addCategoriaToCollectionIfMissing(categorias, ...(this.editForm.get('categorias')!.value ?? []))
        )
      )
      .subscribe((categorias: ICategoria[]) => (this.categoriasSharedCollection = categorias));
  }

  protected createFromForm(): IServicio {
    return {
      ...new Servicio(),
      id: this.editForm.get(['id'])!.value,
      titulo: this.editForm.get(['titulo'])!.value,
      descripcion: this.editForm.get(['descripcion'])!.value,
      disponibilidad: this.editForm.get(['disponibilidad'])!.value,
      preciohora: this.editForm.get(['preciohora'])!.value,
      preciotraslado: this.editForm.get(['preciotraslado'])!.value,
      fechacreacion: this.editForm.get(['fechacreacion'])!.value
        ? dayjs(this.editForm.get(['fechacreacion'])!.value, DATE_TIME_FORMAT)
        : undefined,
      fechaactualizacion: this.editForm.get(['fechaactualizacion'])!.value
        ? dayjs(this.editForm.get(['fechaactualizacion'])!.value, DATE_TIME_FORMAT)
        : undefined,
      destacado: this.editForm.get(['destacado'])!.value,
      usuario: this.editForm.get(['usuario'])!.value,
      categorias: this.editForm.get(['categorias'])!.value,
    };
  }
}
