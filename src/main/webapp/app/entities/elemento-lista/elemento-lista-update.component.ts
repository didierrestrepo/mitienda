import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { JhiDataUtils, JhiFileLoadError, JhiEventManager, JhiEventWithContent } from 'ng-jhipster';

import { IElementoLista, ElementoLista } from 'app/shared/model/elemento-lista.model';
import { ElementoListaService } from './elemento-lista.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { ITipoLista } from 'app/shared/model/tipo-lista.model';
import { TipoListaService } from 'app/entities/tipo-lista/tipo-lista.service';

type SelectableEntity = ITipoLista | IElementoLista;

@Component({
  selector: 'jhi-elemento-lista-update',
  templateUrl: './elemento-lista-update.component.html'
})
export class ElementoListaUpdateComponent implements OnInit {
  isSaving = false;

  tipolistas: ITipoLista[] = [];

  elementolistas: IElementoLista[] = [];

  editForm = this.fb.group({
    id: [],
    nombre: [null, [Validators.required, Validators.maxLength(200)]],
    codigo: [null, [Validators.required, Validators.maxLength(5)]],
    propiedades: [],
    propiedadesContentType: [],
    tipoListaId: [null, Validators.required],
    padreId: []
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected elementoListaService: ElementoListaService,
    protected tipoListaService: TipoListaService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ elementoLista }) => {
      this.updateForm(elementoLista);

      this.tipoListaService
        .query()
        .pipe(
          map((res: HttpResponse<ITipoLista[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: ITipoLista[]) => (this.tipolistas = resBody));

      this.elementoListaService
        .query()
        .pipe(
          map((res: HttpResponse<IElementoLista[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IElementoLista[]) => (this.elementolistas = resBody));
    });
  }

  updateForm(elementoLista: IElementoLista): void {
    this.editForm.patchValue({
      id: elementoLista.id,
      nombre: elementoLista.nombre,
      codigo: elementoLista.codigo,
      propiedades: elementoLista.propiedades,
      propiedadesContentType: elementoLista.propiedadesContentType,
      tipoListaId: elementoLista.tipoListaId,
      padreId: elementoLista.padreId
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType: string, base64String: string): void {
    this.dataUtils.openFile(contentType, base64String);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe(null, (err: JhiFileLoadError) => {
      this.eventManager.broadcast(
        new JhiEventWithContent<AlertError>('mitiendaApp.error', { ...err, key: 'error.file.' + err.key })
      );
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const elementoLista = this.createFromForm();
    if (elementoLista.id !== undefined) {
      this.subscribeToSaveResponse(this.elementoListaService.update(elementoLista));
    } else {
      this.subscribeToSaveResponse(this.elementoListaService.create(elementoLista));
    }
  }

  private createFromForm(): IElementoLista {
    return {
      ...new ElementoLista(),
      id: this.editForm.get(['id'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      codigo: this.editForm.get(['codigo'])!.value,
      propiedadesContentType: this.editForm.get(['propiedadesContentType'])!.value,
      propiedades: this.editForm.get(['propiedades'])!.value,
      tipoListaId: this.editForm.get(['tipoListaId'])!.value,
      padreId: this.editForm.get(['padreId'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IElementoLista>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }
}
