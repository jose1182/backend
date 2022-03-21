import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MensajeDetailComponent } from './mensaje-detail.component';

describe('Mensaje Management Detail Component', () => {
  let comp: MensajeDetailComponent;
  let fixture: ComponentFixture<MensajeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MensajeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ mensaje: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(MensajeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(MensajeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load mensaje on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.mensaje).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
