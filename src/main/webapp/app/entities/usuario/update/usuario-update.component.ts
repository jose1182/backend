import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IUsuario, Usuario } from '../usuario.model';
import { UsuarioService } from '../service/usuario.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IConversacion } from 'app/entities/conversacion/conversacion.model';
import { ConversacionService } from 'app/entities/conversacion/service/conversacion.service';

@Component({
  selector: 'jhi-usuario-update',
  templateUrl: './usuario-update.component.html',
})
export class UsuarioUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];
  conversacionsSharedCollection: IConversacion[] = [];

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
    imagen: [],
    imagenContentType: [],
    descripcion: [],
    codigopostal: [],
    user: [],
    conversacions: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected usuarioService: UsuarioService,
    protected userService: UserService,
    protected conversacionService: ConversacionService,
    protected elementRef: ElementRef,
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

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('netjobApp.error', { message: err.message })),
    });
  }

  clearInputImage(field: string, fieldContentType: string, idInput: string): void {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null,
    });
    if (idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
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

  trackConversacionById(index: number, item: IConversacion): number {
    return item.id!;
  }

  getSelectedConversacion(option: IConversacion, selectedVals?: IConversacion[]): IConversacion {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
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
      imagen: usuario.imagen,
      imagenContentType: usuario.imagenContentType,
      descripcion: usuario.descripcion,
      codigopostal: usuario.codigopostal,
      user: usuario.user,
      conversacions: usuario.conversacions,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, usuario.user);
    this.conversacionsSharedCollection = this.conversacionService.addConversacionToCollectionIfMissing(
      this.conversacionsSharedCollection,
      ...(usuario.conversacions ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.conversacionService
      .query()
      .pipe(map((res: HttpResponse<IConversacion[]>) => res.body ?? []))
      .pipe(
        map((conversacions: IConversacion[]) =>
          this.conversacionService.addConversacionToCollectionIfMissing(conversacions, ...(this.editForm.get('conversacions')!.value ?? []))
        )
      )
      .subscribe((conversacions: IConversacion[]) => (this.conversacionsSharedCollection = conversacions));
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
      imagenContentType: this.editForm.get(['imagenContentType'])!.value,
      imagen: this.editForm.get(['imagen'])!.value,
      descripcion: this.editForm.get(['descripcion'])!.value,
      codigopostal: this.editForm.get(['codigopostal'])!.value,
      user: this.editForm.get(['user'])!.value,
      conversacions: this.editForm.get(['conversacions'])!.value,
    };
  }
}
