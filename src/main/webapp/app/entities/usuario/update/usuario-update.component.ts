import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IUsuario, Usuario } from '../usuario.model';
import { UsuarioService } from '../service/usuario.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'jhi-usuario-update',
  templateUrl: './usuario-update.component.html',
})
export class UsuarioUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    nombre: [null, [Validators.required, Validators.maxLength(40)]],
    apellidos: [null, [Validators.maxLength(50)]],
    correo: [null, [Validators.required, Validators.maxLength(20)]],
    dni: [null, [Validators.required, Validators.pattern('[0-9]{8}[TRWAGMYFPDXBNJZSQVHLCKE]')]],
    direccion: [null, [Validators.required]],
    localidad: [null, [Validators.required]],
    provincia: [null, [Validators.required]],
    profesion: [],
    fn: [null, [Validators.required]],
    fechaRegistro: [],
    user: [],
  });

  constructor(
    protected usuarioService: UsuarioService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ usuario }) => {
      if (usuario.id === undefined) {
        const today = dayjs().startOf('day');
        usuario.fn = today;
        usuario.fechaRegistro = today;
      }

      this.updateForm(usuario);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const usuario = this.createFromForm();
    if (usuario.id !== undefined) {
      this.subscribeToSaveResponse(this.usuarioService.update(usuario));
    } else {
      this.subscribeToSaveResponse(this.usuarioService.create(usuario));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUsuario>>): void {
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

  protected updateForm(usuario: IUsuario): void {
    this.editForm.patchValue({
      id: usuario.id,
      nombre: usuario.nombre,
      apellidos: usuario.apellidos,
      correo: usuario.correo,
      dni: usuario.dni,
      direccion: usuario.direccion,
      localidad: usuario.localidad,
      provincia: usuario.provincia,
      profesion: usuario.profesion,
      fn: usuario.fn ? usuario.fn.format(DATE_TIME_FORMAT) : null,
      fechaRegistro: usuario.fechaRegistro ? usuario.fechaRegistro.format(DATE_TIME_FORMAT) : null,
      user: usuario.user,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, usuario.user);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }

  protected createFromForm(): IUsuario {
    return {
      ...new Usuario(),
      id: this.editForm.get(['id'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      apellidos: this.editForm.get(['apellidos'])!.value,
      correo: this.editForm.get(['correo'])!.value,
      dni: this.editForm.get(['dni'])!.value,
      direccion: this.editForm.get(['direccion'])!.value,
      localidad: this.editForm.get(['localidad'])!.value,
      provincia: this.editForm.get(['provincia'])!.value,
      profesion: this.editForm.get(['profesion'])!.value,
      fn: this.editForm.get(['fn'])!.value ? dayjs(this.editForm.get(['fn'])!.value, DATE_TIME_FORMAT) : undefined,
      fechaRegistro: this.editForm.get(['fechaRegistro'])!.value
        ? dayjs(this.editForm.get(['fechaRegistro'])!.value, DATE_TIME_FORMAT)
        : undefined,
      user: this.editForm.get(['user'])!.value,
    };
  }
}
