import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ConversacionDetailComponent } from './conversacion-detail.component';

describe('Conversacion Management Detail Component', () => {
  let comp: ConversacionDetailComponent;
  let fixture: ComponentFixture<ConversacionDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ConversacionDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ conversacion: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ConversacionDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ConversacionDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load conversacion on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.conversacion).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
