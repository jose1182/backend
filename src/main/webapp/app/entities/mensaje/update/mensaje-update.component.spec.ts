import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { MensajeService } from '../service/mensaje.service';
import { IMensaje, Mensaje } from '../mensaje.model';
import { IUsuario } from 'app/entities/usuario/usuario.model';
import { UsuarioService } from 'app/entities/usuario/service/usuario.service';
import { IConversacion } from 'app/entities/conversacion/conversacion.model';
import { ConversacionService } from 'app/entities/conversacion/service/conversacion.service';

import { MensajeUpdateComponent } from './mensaje-update.component';

describe('Mensaje Management Update Component', () => {
  let comp: MensajeUpdateComponent;
  let fixture: ComponentFixture<MensajeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let mensajeService: MensajeService;
  let usuarioService: UsuarioService;
  let conversacionService: ConversacionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [MensajeUpdateComponent],
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
      .overrideTemplate(MensajeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MensajeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    mensajeService = TestBed.inject(MensajeService);
    usuarioService = TestBed.inject(UsuarioService);
    conversacionService = TestBed.inject(ConversacionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Usuario query and add missing value', () => {
      const mensaje: IMensaje = { id: 456 };
      const emisor: IUsuario = { id: 90022 };
      mensaje.emisor = emisor;
      const receptor: IUsuario = { id: 55237 };
      mensaje.receptor = receptor;

      const usuarioCollection: IUsuario[] = [{ id: 38320 }];
      jest.spyOn(usuarioService, 'query').mockReturnValue(of(new HttpResponse({ body: usuarioCollection })));
      const additionalUsuarios = [emisor, receptor];
      const expectedCollection: IUsuario[] = [...additionalUsuarios, ...usuarioCollection];
      jest.spyOn(usuarioService, 'addUsuarioToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ mensaje });
      comp.ngOnInit();

      expect(usuarioService.query).toHaveBeenCalled();
      expect(usuarioService.addUsuarioToCollectionIfMissing).toHaveBeenCalledWith(usuarioCollection, ...additionalUsuarios);
      expect(comp.usuariosSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Conversacion query and add missing value', () => {
      const mensaje: IMensaje = { id: 456 };
      const conversacion: IConversacion = { id: 51968 };
      mensaje.conversacion = conversacion;

      const conversacionCollection: IConversacion[] = [{ id: 750 }];
      jest.spyOn(conversacionService, 'query').mockReturnValue(of(new HttpResponse({ body: conversacionCollection })));
      const additionalConversacions = [conversacion];
      const expectedCollection: IConversacion[] = [...additionalConversacions, ...conversacionCollection];
      jest.spyOn(conversacionService, 'addConversacionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ mensaje });
      comp.ngOnInit();

      expect(conversacionService.query).toHaveBeenCalled();
      expect(conversacionService.addConversacionToCollectionIfMissing).toHaveBeenCalledWith(
        conversacionCollection,
        ...additionalConversacions
      );
      expect(comp.conversacionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const mensaje: IMensaje = { id: 456 };
      const emisor: IUsuario = { id: 78244 };
      mensaje.emisor = emisor;
      const receptor: IUsuario = { id: 57922 };
      mensaje.receptor = receptor;
      const conversacion: IConversacion = { id: 62829 };
      mensaje.conversacion = conversacion;

      activatedRoute.data = of({ mensaje });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(mensaje));
      expect(comp.usuariosSharedCollection).toContain(emisor);
      expect(comp.usuariosSharedCollection).toContain(receptor);
      expect(comp.conversacionsSharedCollection).toContain(conversacion);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Mensaje>>();
      const mensaje = { id: 123 };
      jest.spyOn(mensajeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ mensaje });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: mensaje }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(mensajeService.update).toHaveBeenCalledWith(mensaje);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Mensaje>>();
      const mensaje = new Mensaje();
      jest.spyOn(mensajeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ mensaje });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: mensaje }));
      saveSubject.complete();

      // THEN
      expect(mensajeService.create).toHaveBeenCalledWith(mensaje);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Mensaje>>();
      const mensaje = { id: 123 };
      jest.spyOn(mensajeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ mensaje });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(mensajeService.update).toHaveBeenCalledWith(mensaje);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackUsuarioById', () => {
      it('Should return tracked Usuario primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackUsuarioById(0, entity);
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
});
