import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { ValoracionService } from '../service/valoracion.service';

import { ValoracionComponent } from './valoracion.component';

describe('Valoracion Management Component', () => {
  let comp: ValoracionComponent;
  let fixture: ComponentFixture<ValoracionComponent>;
  let service: ValoracionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ValoracionComponent],
    })
      .overrideTemplate(ValoracionComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ValoracionComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ValoracionService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.valoracions?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
