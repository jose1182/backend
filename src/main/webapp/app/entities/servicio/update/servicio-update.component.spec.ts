import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ServicioService } from '../service/servicio.service';
import { IServicio, Servicio } from '../servicio.model';
import { IUsuario } from 'app/entities/usuario/usuario.model';
import { UsuarioService } from 'app/entities/usuario/service/usuario.service';
import { ICategoria } from 'app/entities/categoria/categoria.model';
import { CategoriaService } from 'app/entities/categoria/service/categoria.service';

import { ServicioUpdateComponent } from './servicio-update.component';

describe('Servicio Management Update Component', () => {
  let comp: ServicioUpdateComponent;
  let fixture: ComponentFixture<ServicioUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let servicioService: ServicioService;
  let usuarioService: UsuarioService;
  let categoriaService: CategoriaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ServicioUpdateComponent],
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
      .overrideTemplate(ServicioUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ServicioUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    servicioService = TestBed.inject(ServicioService);
    usuarioService = TestBed.inject(UsuarioService);
    categoriaService = TestBed.inject(CategoriaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Usuario query and add missing value', () => {
      const servicio: IServicio = { id: 456 };
      const usuario: IUsuario = { id: 40044 };
      servicio.usuario = usuario;

      const usuarioCollection: IUsuario[] = [{ id: 44930 }];
      jest.spyOn(usuarioService, 'query').mockReturnValue(of(new HttpResponse({ body: usuarioCollection })));
      const additionalUsuarios = [usuario];
      const expectedCollection: IUsuario[] = [...additionalUsuarios, ...usuarioCollection];
      jest.spyOn(usuarioService, 'addUsuarioToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ servicio });
      comp.ngOnInit();

      expect(usuarioService.query).toHaveBeenCalled();
      expect(usuarioService.addUsuarioToCollectionIfMissing).toHaveBeenCalledWith(usuarioCollection, ...additionalUsuarios);
      expect(comp.usuariosSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Categoria query and add missing value', () => {
      const servicio: IServicio = { id: 456 };
      const categorias: ICategoria[] = [{ id: 44452 }];
      servicio.categorias = categorias;

      const categoriaCollection: ICategoria[] = [{ id: 51369 }];
      jest.spyOn(categoriaService, 'query').mockReturnValue(of(new HttpResponse({ body: categoriaCollection })));
      const additionalCategorias = [...categorias];
      const expectedCollection: ICategoria[] = [...additionalCategorias, ...categoriaCollection];
      jest.spyOn(categoriaService, 'addCategoriaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ servicio });
      comp.ngOnInit();

      expect(categoriaService.query).toHaveBeenCalled();
      expect(categoriaService.addCategoriaToCollectionIfMissing).toHaveBeenCalledWith(categoriaCollection, ...additionalCategorias);
      expect(comp.categoriasSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const servicio: IServicio = { id: 456 };
      const usuario: IUsuario = { id: 98648 };
      servicio.usuario = usuario;
      const categorias: ICategoria = { id: 37068 };
      servicio.categorias = [categorias];

      activatedRoute.data = of({ servicio });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(servicio));
      expect(comp.usuariosSharedCollection).toContain(usuario);
      expect(comp.categoriasSharedCollection).toContain(categorias);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Servicio>>();
      const servicio = { id: 123 };
      jest.spyOn(servicioService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ servicio });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: servicio }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(servicioService.update).toHaveBeenCalledWith(servicio);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Servicio>>();
      const servicio = new Servicio();
      jest.spyOn(servicioService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ servicio });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: servicio }));
      saveSubject.complete();

      // THEN
      expect(servicioService.create).toHaveBeenCalledWith(servicio);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Servicio>>();
      const servicio = { id: 123 };
      jest.spyOn(servicioService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ servicio });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(servicioService.update).toHaveBeenCalledWith(servicio);
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

    describe('trackCategoriaById', () => {
      it('Should return tracked Categoria primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCategoriaById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });

  describe('Getting selected relationships', () => {
    describe('getSelectedCategoria', () => {
      it('Should return option if no Categoria is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedCategoria(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected Categoria for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedCategoria(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this Categoria is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedCategoria(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
