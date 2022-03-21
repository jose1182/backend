import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IConversacion, Conversacion } from '../conversacion.model';
import { ConversacionService } from '../service/conversacion.service';

@Component({
  selector: 'jhi-conversacion-update',
  templateUrl: './conversacion-update.component.html',
})
export class ConversacionUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
  });

  constructor(protected conversacionService: ConversacionService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ conversacion }) => {
      this.updateForm(conversacion);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const conversacion = this.createFromForm();
    if (conversacion.id !== undefined) {
      this.subscribeToSaveResponse(this.conversacionService.update(conversacion));
    } else {
      this.subscribeToSaveResponse(this.conversacionService.create(conversacion));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IConversacion>>): void {
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

  protected updateForm(conversacion: IConversacion): void {
    this.editForm.patchValue({
      id: conversacion.id,
    });
  }

  protected createFromForm(): IConversacion {
    return {
      ...new Conversacion(),
      id: this.editForm.get(['id'])!.value,
    };
  }
}
