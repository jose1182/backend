import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { UsuarioService } from '../service/usuario.service';
import { IUsuario, Usuario } from '../usuario.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IConversacion } from 'app/entities/conversacion/conversacion.model';
import { ConversacionService } from 'app/entities/conversacion/service/conversacion.service';

import { UsuarioUpdateComponent } from './usuario-update.component';

describe('Usuario Management Update Component', () => {
  let comp: UsuarioUpdateComponent;
  let fixture: ComponentFixture<UsuarioUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let usuarioService: UsuarioService;
  let userService: UserService;
  let conversacionService: ConversacionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [UsuarioUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(UsuarioUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(UsuarioUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    usuarioService = TestBed.inject(UsuarioService);
    userService = TestBed.inject(UserService);
    conversacionService = TestBed.inject(ConversacionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const usuario: IUsuario = { id: 456 };
      const user: IUser = { id: 13812 };
      usuario.user = user;

      const userCollection: IUser[] = [{ id: 98506 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ usuario });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Conversacion query and add missing value', () => {
      const usuario: IUsuario = { id: 456 };
      const conversacions: IConversacion[] = [{ id: 92100 }];
      usuario.conversacions = conversacions;

      const conversacionCollection: IConversacion[] = [{ id: 39259 }];
      jest.spyOn(conversacionService, 'query').mockReturnValue(of(new HttpResponse({ body: conversacionCollection })));
      const additionalConversacions = [...conversacions];
      const expectedCollection: IConversacion[] = [...additionalConversacions, ...conversacionCollection];
      jest.spyOn(conversacionService, 'addConversacionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ usuario });
      comp.ngOnInit();

      expect(conversacionService.query).toHaveBeenCalled();
      expect(conversacionService.addConversacionToCollectionIfMissing).toHaveBeenCalledWith(
        conversacionCollection,
        ...additionalConversacions
      );
      expect(comp.conversacionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const usuario: IUsuario = { id: 456 };
      const user: IUser = { id: 95854 };
      usuario.user = user;
      const conversacions: IConversacion = { id: 37972 };
      usuario.conversacions = [conversacions];

      activatedRoute.data = of({ usuario });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(usuario));
      expect(comp.usersSharedCollection).toContain(user);
      expect(comp.conversacionsSharedCollection).toContain(conversacions);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Usuario>>();
      const usuario = { id: 123 };
      jest.spyOn(usuarioService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ usuario });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: usuario }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(usuarioService.update).toHaveBeenCalledWith(usuario);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Usuario>>();
      const usuario = new Usuario();
      jest.spyOn(usuarioService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ usuario });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: usuario }));
      saveSubject.complete();

      // THEN
      expect(usuarioService.create).toHaveBeenCalledWith(usuario);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Usuario>>();
      const usuario = { id: 123 };
      jest.spyOn(usuarioService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ usuario });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(usuarioService.update).toHaveBeenCalledWith(usuario);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackUserById', () => {
      it('Should return tracked User primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackUserById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackConversacionById', () => {
      it('Should return tracked Conversacion primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackConversacionById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });

  describe('Getting selected relationships', () => {
    describe('getSelectedConversacion', () => {
      it('Should return option if no Conversacion is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedConversacion(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected Conversacion for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedConversacion(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this Conversacion is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedConversacion(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
