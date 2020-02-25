import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { MitiendaTestModule } from '../../../test.module';
import { ProductoDetalleUpdateComponent } from 'app/entities/producto-detalle/producto-detalle-update.component';
import { ProductoDetalleService } from 'app/entities/producto-detalle/producto-detalle.service';
import { ProductoDetalle } from 'app/shared/model/producto-detalle.model';

describe('Component Tests', () => {
  describe('ProductoDetalle Management Update Component', () => {
    let comp: ProductoDetalleUpdateComponent;
    let fixture: ComponentFixture<ProductoDetalleUpdateComponent>;
    let service: ProductoDetalleService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MitiendaTestModule],
        declarations: [ProductoDetalleUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(ProductoDetalleUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ProductoDetalleUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ProductoDetalleService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new ProductoDetalle(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new ProductoDetalle();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
