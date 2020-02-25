import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { MitiendaTestModule } from '../../../test.module';
import { TipoListaUpdateComponent } from 'app/entities/tipo-lista/tipo-lista-update.component';
import { TipoListaService } from 'app/entities/tipo-lista/tipo-lista.service';
import { TipoLista } from 'app/shared/model/tipo-lista.model';

describe('Component Tests', () => {
  describe('TipoLista Management Update Component', () => {
    let comp: TipoListaUpdateComponent;
    let fixture: ComponentFixture<TipoListaUpdateComponent>;
    let service: TipoListaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MitiendaTestModule],
        declarations: [TipoListaUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(TipoListaUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TipoListaUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(TipoListaService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new TipoLista(123);
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
        const entity = new TipoLista();
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
