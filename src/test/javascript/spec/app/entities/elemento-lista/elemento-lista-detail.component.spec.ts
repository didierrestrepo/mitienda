import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { JhiDataUtils } from 'ng-jhipster';

import { MitiendaTestModule } from '../../../test.module';
import { ElementoListaDetailComponent } from 'app/entities/elemento-lista/elemento-lista-detail.component';
import { ElementoLista } from 'app/shared/model/elemento-lista.model';

describe('Component Tests', () => {
  describe('ElementoLista Management Detail Component', () => {
    let comp: ElementoListaDetailComponent;
    let fixture: ComponentFixture<ElementoListaDetailComponent>;
    let dataUtils: JhiDataUtils;
    const route = ({ data: of({ elementoLista: new ElementoLista(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MitiendaTestModule],
        declarations: [ElementoListaDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(ElementoListaDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ElementoListaDetailComponent);
      comp = fixture.componentInstance;
      dataUtils = fixture.debugElement.injector.get(JhiDataUtils);
    });

    describe('OnInit', () => {
      it('Should load elementoLista on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.elementoLista).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });

    describe('byteSize', () => {
      it('Should call byteSize from JhiDataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'byteSize');
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.byteSize(fakeBase64);

        // THEN
        expect(dataUtils.byteSize).toBeCalledWith(fakeBase64);
      });
    });

    describe('openFile', () => {
      it('Should call openFile from JhiDataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'openFile');
        const fakeContentType = 'fake content type';
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.openFile(fakeContentType, fakeBase64);

        // THEN
        expect(dataUtils.openFile).toBeCalledWith(fakeContentType, fakeBase64);
      });
    });
  });
});
