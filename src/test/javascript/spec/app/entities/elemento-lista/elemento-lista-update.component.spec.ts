import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { MitiendaTestModule } from '../../../test.module';
import { ElementoListaUpdateComponent } from 'app/entities/elemento-lista/elemento-lista-update.component';
import { ElementoListaService } from 'app/entities/elemento-lista/elemento-lista.service';
import { ElementoLista } from 'app/shared/model/elemento-lista.model';

describe('Component Tests', () => {
  describe('ElementoLista Management Update Component', () => {
    let comp: ElementoListaUpdateComponent;
    let fixture: ComponentFixture<ElementoListaUpdateComponent>;
    let service: ElementoListaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MitiendaTestModule],
        declarations: [ElementoListaUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(ElementoListaUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ElementoListaUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ElementoListaService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new ElementoLista(123);
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
        const entity = new ElementoLista();
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
