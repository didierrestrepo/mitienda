import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MitiendaTestModule } from '../../../test.module';
import { TipoListaDetailComponent } from 'app/entities/tipo-lista/tipo-lista-detail.component';
import { TipoLista } from 'app/shared/model/tipo-lista.model';

describe('Component Tests', () => {
  describe('TipoLista Management Detail Component', () => {
    let comp: TipoListaDetailComponent;
    let fixture: ComponentFixture<TipoListaDetailComponent>;
    const route = ({ data: of({ tipoLista: new TipoLista(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MitiendaTestModule],
        declarations: [TipoListaDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(TipoListaDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TipoListaDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load tipoLista on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.tipoLista).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
