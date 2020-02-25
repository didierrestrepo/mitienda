import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Data } from '@angular/router';

import { MitiendaTestModule } from '../../../test.module';
import { ElementoListaComponent } from 'app/entities/elemento-lista/elemento-lista.component';
import { ElementoListaService } from 'app/entities/elemento-lista/elemento-lista.service';
import { ElementoLista } from 'app/shared/model/elemento-lista.model';

describe('Component Tests', () => {
  describe('ElementoLista Management Component', () => {
    let comp: ElementoListaComponent;
    let fixture: ComponentFixture<ElementoListaComponent>;
    let service: ElementoListaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MitiendaTestModule],
        declarations: [ElementoListaComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: {
              data: {
                subscribe: (fn: (value: Data) => void) =>
                  fn({
                    pagingParams: {
                      predicate: 'id',
                      reverse: false,
                      page: 0
                    }
                  })
              }
            }
          }
        ]
      })
        .overrideTemplate(ElementoListaComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ElementoListaComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ElementoListaService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new ElementoLista(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.elementoListas && comp.elementoListas[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });

    it('should load a page', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new ElementoLista(123)],
            headers
          })
        )
      );

      // WHEN
      comp.loadPage(1);

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.elementoListas && comp.elementoListas[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });

    it('should calculate the sort attribute for an id', () => {
      // WHEN
      comp.ngOnInit();
      const result = comp.sort();

      // THEN
      expect(result).toEqual(['id,desc']);
    });

    it('should calculate the sort attribute for a non-id attribute', () => {
      // INIT
      comp.ngOnInit();

      // GIVEN
      comp.predicate = 'name';

      // WHEN
      const result = comp.sort();

      // THEN
      expect(result).toEqual(['name,desc', 'id']);
    });
  });
});
