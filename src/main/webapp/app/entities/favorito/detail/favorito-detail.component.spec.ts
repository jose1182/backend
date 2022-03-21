import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FavoritoDetailComponent } from './favorito-detail.component';

describe('Favorito Management Detail Component', () => {
  let comp: FavoritoDetailComponent;
  let fixture: ComponentFixture<FavoritoDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FavoritoDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ favorito: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(FavoritoDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(FavoritoDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load favorito on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.favorito).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
