import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IFavorito, Favorito } from '../favorito.model';
import { FavoritoService } from '../service/favorito.service';
import { IUsuario } from 'app/entities/usuario/usuario.model';
import { UsuarioService } from 'app/entities/usuario/service/usuario.service';
import { IServicio } from 'app/entities/servicio/servicio.model';
import { ServicioService } from 'app/entities/servicio/service/servicio.service';

@Component({
  selector: 'jhi-favorito-update',
  templateUrl: './favorito-update.component.html',
})
export class FavoritoUpdateComponent implements OnInit {
  isSaving = false;

  usuariosSharedCollection: IUsuario[] = [];
  serviciosSharedCollection: IServicio[] = [];

  editForm = this.fb.group({
    id: [],
    usuario: [],
    servicio: [],
  });

  constructor(
    protected favoritoService: FavoritoService,
    protected usuarioService: UsuarioService,
    protected servicioService: ServicioService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ favorito }) => {
      this.updateForm(favorito);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const favorito = this.createFromForm();
    if (favorito.id !== undefined) {
      this.subscribeToSaveResponse(this.favoritoService.update(favorito));
    } else {
      this.subscribeToSaveResponse(this.favoritoService.create(favorito));
    }
  }

  trackUsuarioById(index: number, item: IUsuario): number {
    return item.id!;
  }

  trackServicioById(index: number, item: IServicio): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFavorito>>): void {
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

  protected updateForm(favorito: IFavorito): void {
    this.editForm.patchValue({
      id: favorito.id,
      usuario: favorito.usuario,
      servicio: favorito.servicio,
    });

    this.usuariosSharedCollection = this.usuarioService.addUsuarioToCollectionIfMissing(this.usuariosSharedCollection, favorito.usuario);
    this.serviciosSharedCollection = this.servicioService.addServicioToCollectionIfMissing(
      this.serviciosSharedCollection,
      favorito.servicio
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

  protected createFromForm(): IFavorito {
    return {
      ...new Favorito(),
      id: this.editForm.get(['id'])!.value,
      usuario: this.editForm.get(['usuario'])!.value,
      servicio: this.editForm.get(['servicio'])!.value,
    };
  }
}
