import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ContratoService } from '../service/contrato.service';
import { IContrato, Contrato } from '../contrato.model';
import { IUsuario } from 'app/entities/usuario/usuario.model';
import { UsuarioService } from 'app/entities/usuario/service/usuario.service';
import { IServicio } from 'app/entities/servicio/servicio.model';
import { ServicioService } from 'app/entities/servicio/service/servicio.service';

import { ContratoUpdateComponent } from './contrato-update.component';

describe('Contrato Management Update Component', () => {
  let comp: ContratoUpdateComponent;
  let fixture: ComponentFixture<ContratoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let contratoService: ContratoService;
  let usuarioService: UsuarioService;
  let servicioService: ServicioService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ContratoUpdateComponent],
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
      .overrideTemplate(ContratoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ContratoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    contratoService = TestBed.inject(ContratoService);
    usuarioService = TestBed.inject(UsuarioService);
    servicioService = TestBed.inject(ServicioService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Usuario query and add missing value', () => {
      const contrato: IContrato = { id: 456 };
      const usuario: IUsuario = { id: 68594 };
      contrato.usuario = usuario;

      const usuarioCollection: IUsuario[] = [{ id: 13922 }];
      jest.spyOn(usuarioService, 'query').mockReturnValue(of(new HttpResponse({ body: usuarioCollection })));
      const additionalUsuarios = [usuario];
      const expectedCollection: IUsuario[] = [...additionalUsuarios, ...usuarioCollection];
      jest.spyOn(usuarioService, 'addUsuarioToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ contrato });
      comp.ngOnInit();

      expect(usuarioService.query).toHaveBeenCalled();
      expect(usuarioService.addUsuarioToCollectionIfMissing).toHaveBeenCalledWith(usuarioCollection, ...additionalUsuarios);
      expect(comp.usuariosSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Servicio query and add missing value', () => {
      const contrato: IContrato = { id: 456 };
      const servicio: IServicio = { id: 73042 };
      contrato.servicio = servicio;

      const servicioCollection: IServicio[] = [{ id: 16223 }];
      jest.spyOn(servicioService, 'query').mockReturnValue(of(new HttpResponse({ body: servicioCollection })));
      const additionalServicios = [servicio];
      const expectedCollection: IServicio[] = [...additionalServicios, ...servicioCollection];
      jest.spyOn(servicioService, 'addServicioToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ contrato });
      comp.ngOnInit();

      expect(servicioService.query).toHaveBeenCalled();
      expect(servicioService.addServicioToCollectionIfMissing).toHaveBeenCalledWith(servicioCollection, ...additionalServicios);
      expect(comp.serviciosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const contrato: IContrato = { id: 456 };
      const usuario: IUsuario = { id: 75949 };
      contrato.usuario = usuario;
      const servicio: IServicio = { id: 7283 };
      contrato.servicio = servicio;

      activatedRoute.data = of({ contrato });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(contrato));
      expect(comp.usuariosSharedCollection).toContain(usuario);
      expect(comp.serviciosSharedCollection).toContain(servicio);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Contrato>>();
      const contrato = { id: 123 };
      jest.spyOn(contratoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contrato });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: contrato }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(contratoService.update).toHaveBeenCalledWith(contrato);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Contrato>>();
      const contrato = new Contrato();
      jest.spyOn(contratoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contrato });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: contrato }));
      saveSubject.complete();

      // THEN
      expect(contratoService.create).toHaveBeenCalledWith(contrato);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Contrato>>();
      const contrato = { id: 123 };
      jest.spyOn(contratoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contrato });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(contratoService.update).toHaveBeenCalledWith(contrato);
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

    describe('trackServicioById', () => {
      it('Should return tracked Servicio primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackServicioById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
