import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IValoracion, Valoracion } from '../valoracion.model';
import { ValoracionService } from '../service/valoracion.service';

@Component({
  selector: 'jhi-valoracion-update',
  templateUrl: './valoracion-update.component.html',
})
export class ValoracionUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    descripcion: [null, [Validators.required, Validators.minLength(3), Validators.maxLength(500)]],
    fecha: [null, [Validators.required]],
    id_servicio: [],
  });

  constructor(protected valoracionService: ValoracionService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ valoracion }) => {
      if (valoracion.id === undefined) {
        const today = dayjs().startOf('day');
        valoracion.fecha = today;
      }

      this.updateForm(valoracion);
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
      id_servicio: valoracion.id_servicio,
    });
  }

  protected createFromForm(): IValoracion {
    return {
      ...new Valoracion(),
      id: this.editForm.get(['id'])!.value,
      descripcion: this.editForm.get(['descripcion'])!.value,
      fecha: this.editForm.get(['fecha'])!.value ? dayjs(this.editForm.get(['fecha'])!.value, DATE_TIME_FORMAT) : undefined,
      id_servicio: this.editForm.get(['id_servicio'])!.value,
    };
  }
}
