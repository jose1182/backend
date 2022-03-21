import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { FavoritoService } from '../service/favorito.service';
import { IFavorito, Favorito } from '../favorito.model';
import { IUsuario } from 'app/entities/usuario/usuario.model';
import { UsuarioService } from 'app/entities/usuario/service/usuario.service';
import { IServicio } from 'app/entities/servicio/servicio.model';
import { ServicioService } from 'app/entities/servicio/service/servicio.service';

import { FavoritoUpdateComponent } from './favorito-update.component';

describe('Favorito Management Update Component', () => {
  let comp: FavoritoUpdateComponent;
  let fixture: ComponentFixture<FavoritoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let favoritoService: FavoritoService;
  let usuarioService: UsuarioService;
  let servicioService: ServicioService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [FavoritoUpdateComponent],
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
      .overrideTemplate(FavoritoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FavoritoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    favoritoService = TestBed.inject(FavoritoService);
    usuarioService = TestBed.inject(UsuarioService);
    servicioService = TestBed.inject(ServicioService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Usuario query and add missing value', () => {
      const favorito: IFavorito = { id: 456 };
      const usuario: IUsuario = { id: 30961 };
      favorito.usuario = usuario;

      const usuarioCollection: IUsuario[] = [{ id: 4362 }];
      jest.spyOn(usuarioService, 'query').mockReturnValue(of(new HttpResponse({ body: usuarioCollection })));
      const additionalUsuarios = [usuario];
      const expectedCollection: IUsuario[] = [...additionalUsuarios, ...usuarioCollection];
      jest.spyOn(usuarioService, 'addUsuarioToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ favorito });
      comp.ngOnInit();

      expect(usuarioService.query).toHaveBeenCalled();
      expect(usuarioService.addUsuarioToCollectionIfMissing).toHaveBeenCalledWith(usuarioCollection, ...additionalUsuarios);
      expect(comp.usuariosSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Servicio query and add missing value', () => {
      const favorito: IFavorito = { id: 456 };
      const servicio: IServicio = { id: 34587 };
      favorito.servicio = servicio;

      const servicioCollection: IServicio[] = [{ id: 59802 }];
      jest.spyOn(servicioService, 'query').mockReturnValue(of(new HttpResponse({ body: servicioCollection })));
      const additionalServicios = [servicio];
      const expectedCollection: IServicio[] = [...additionalServicios, ...servicioCollection];
      jest.spyOn(servicioService, 'addServicioToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ favorito });
      comp.ngOnInit();

      expect(servicioService.query).toHaveBeenCalled();
      expect(servicioService.addServicioToCollectionIfMissing).toHaveBeenCalledWith(servicioCollection, ...additionalServicios);
      expect(comp.serviciosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const favorito: IFavorito = { id: 456 };
      const usuario: IUsuario = { id: 19303 };
      favorito.usuario = usuario;
      const servicio: IServicio = { id: 94218 };
      favorito.servicio = servicio;

      activatedRoute.data = of({ favorito });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(favorito));
      expect(comp.usuariosSharedCollection).toContain(usuario);
      expect(comp.serviciosSharedCollection).toContain(servicio);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Favorito>>();
      const favorito = { id: 123 };
      jest.spyOn(favoritoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ favorito });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: favorito }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(favoritoService.update).toHaveBeenCalledWith(favorito);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Favorito>>();
      const favorito = new Favorito();
      jest.spyOn(favoritoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ favorito });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: favorito }));
      saveSubject.complete();

      // THEN
      expect(favoritoService.create).toHaveBeenCalledWith(favorito);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Favorito>>();
      const favorito = { id: 123 };
      jest.spyOn(favoritoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ favorito });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(favoritoService.update).toHaveBeenCalledWith(favorito);
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
