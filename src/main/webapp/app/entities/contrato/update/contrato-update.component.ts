import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IContrato, Contrato } from '../contrato.model';
import { ContratoService } from '../service/contrato.service';
import { IUsuario } from 'app/entities/usuario/usuario.model';
import { UsuarioService } from 'app/entities/usuario/service/usuario.service';
import { IServicio } from 'app/entities/servicio/servicio.model';
import { ServicioService } from 'app/entities/servicio/service/servicio.service';

@Component({
  selector: 'jhi-contrato-update',
  templateUrl: './contrato-update.component.html',
})
export class ContratoUpdateComponent implements OnInit {
  isSaving = false;

  usuariosSharedCollection: IUsuario[] = [];
  serviciosSharedCollection: IServicio[] = [];

  editForm = this.fb.group({
    id: [],
    preciofinal: [null, [Validators.required, Validators.min(0)]],
    fecha: [null, [Validators.required]],
    usuario: [],
    servicio: [],
  });

  constructor(
    protected contratoService: ContratoService,
    protected usuarioService: UsuarioService,
    protected servicioService: ServicioService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ contrato }) => {
      if (contrato.id === undefined) {
        const today = dayjs().startOf('day');
        contrato.fecha = today;
      }

      this.updateForm(contrato);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const contrato = this.createFromForm();
    if (contrato.id !== undefined) {
      this.subscribeToSaveResponse(this.contratoService.update(contrato));
    } else {
      this.subscribeToSaveResponse(this.contratoService.create(contrato));
    }
  }

  trackUsuarioById(index: number, item: IUsuario): number {
    return item.id!;
  }

  trackServicioById(index: number, item: IServicio): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IContrato>>): void {
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

  protected updateForm(contrato: IContrato): void {
    this.editForm.patchValue({
      id: contrato.id,
      preciofinal: contrato.preciofinal,
      fecha: contrato.fecha ? contrato.fecha.format(DATE_TIME_FORMAT) : null,
      usuario: contrato.usuario,
      servicio: contrato.servicio,
    });

    this.usuariosSharedCollection = this.usuarioService.addUsuarioToCollectionIfMissing(this.usuariosSharedCollection, contrato.usuario);
    this.serviciosSharedCollection = this.servicioService.addServicioToCollectionIfMissing(
      this.serviciosSharedCollection,
      contrato.servicio
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

  protected createFromForm(): IContrato {
    return {
      ...new Contrato(),
      id: this.editForm.get(['id'])!.value,
      preciofinal: this.editForm.get(['preciofinal'])!.value,
      fecha: this.editForm.get(['fecha'])!.value ? dayjs(this.editForm.get(['fecha'])!.value, DATE_TIME_FORMAT) : undefined,
      usuario: this.editForm.get(['usuario'])!.value,
      servicio: this.editForm.get(['servicio'])!.value,
    };
  }
}
