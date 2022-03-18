import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ValoracionService } from '../service/valoracion.service';
import { IValoracion, Valoracion } from '../valoracion.model';

import { ValoracionUpdateComponent } from './valoracion-update.component';

describe('Valoracion Management Update Component', () => {
  let comp: ValoracionUpdateComponent;
  let fixture: ComponentFixture<ValoracionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let valoracionService: ValoracionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ValoracionUpdateComponent],
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
      .overrideTemplate(ValoracionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ValoracionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    valoracionService = TestBed.inject(ValoracionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const valoracion: IValoracion = { id: 456 };

      activatedRoute.data = of({ valoracion });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(valoracion));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Valoracion>>();
      const valoracion = { id: 123 };
      jest.spyOn(valoracionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ valoracion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: valoracion }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(valoracionService.update).toHaveBeenCalledWith(valoracion);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Valoracion>>();
      const valoracion = new Valoracion();
      jest.spyOn(valoracionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ valoracion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: valoracion }));
      saveSubject.complete();

      // THEN
      expect(valoracionService.create).toHaveBeenCalledWith(valoracion);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Valoracion>>();
      const valoracion = { id: 123 };
      jest.spyOn(valoracionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ valoracion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(valoracionService.update).toHaveBeenCalledWith(valoracion);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
