import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ConversacionService } from '../service/conversacion.service';
import { IConversacion, Conversacion } from '../conversacion.model';

import { ConversacionUpdateComponent } from './conversacion-update.component';

describe('Conversacion Management Update Component', () => {
  let comp: ConversacionUpdateComponent;
  let fixture: ComponentFixture<ConversacionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let conversacionService: ConversacionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ConversacionUpdateComponent],
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
      .overrideTemplate(ConversacionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ConversacionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    conversacionService = TestBed.inject(ConversacionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const conversacion: IConversacion = { id: 456 };

      activatedRoute.data = of({ conversacion });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(conversacion));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Conversacion>>();
      const conversacion = { id: 123 };
      jest.spyOn(conversacionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ conversacion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: conversacion }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(conversacionService.update).toHaveBeenCalledWith(conversacion);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Conversacion>>();
      const conversacion = new Conversacion();
      jest.spyOn(conversacionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ conversacion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: conversacion }));
      saveSubject.complete();

      // THEN
      expect(conversacionService.create).toHaveBeenCalledWith(conversacion);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Conversacion>>();
      const conversacion = { id: 123 };
      jest.spyOn(conversacionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ conversacion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(conversacionService.update).toHaveBeenCalledWith(conversacion);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
