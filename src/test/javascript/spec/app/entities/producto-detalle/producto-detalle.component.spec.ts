import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Data } from '@angular/router';

import { MitiendaTestModule } from '../../../test.module';
import { ProductoDetalleComponent } from 'app/entities/producto-detalle/producto-detalle.component';
import { ProductoDetalleService } from 'app/entities/producto-detalle/producto-detalle.service';
import { ProductoDetalle } from 'app/shared/model/producto-detalle.model';

describe('Component Tests', () => {
  describe('ProductoDetalle Management Component', () => {
    let comp: ProductoDetalleComponent;
    let fixture: ComponentFixture<ProductoDetalleComponent>;
    let service: ProductoDetalleService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MitiendaTestModule],
        declarations: [ProductoDetalleComponent],
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
        .overrideTemplate(ProductoDetalleComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ProductoDetalleComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ProductoDetalleService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new ProductoDetalle(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.productoDetalles && comp.productoDetalles[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });

    it('should load a page', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new ProductoDetalle(123)],
            headers
          })
        )
      );

      // WHEN
      comp.loadPage(1);

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.productoDetalles && comp.productoDetalles[0]).toEqual(jasmine.objectContaining({ id: 123 }));
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
