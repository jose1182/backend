import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ValoracionDetailComponent } from './valoracion-detail.component';

describe('Valoracion Management Detail Component', () => {
  let comp: ValoracionDetailComponent;
  let fixture: ComponentFixture<ValoracionDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ValoracionDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ valoracion: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ValoracionDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ValoracionDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load valoracion on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.valoracion).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
