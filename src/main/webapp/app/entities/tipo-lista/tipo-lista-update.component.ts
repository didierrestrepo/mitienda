import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { ITipoLista, TipoLista } from 'app/shared/model/tipo-lista.model';
import { TipoListaService } from './tipo-lista.service';

@Component({
  selector: 'jhi-tipo-lista-update',
  templateUrl: './tipo-lista-update.component.html'
})
export class TipoListaUpdateComponent implements OnInit {
  isSaving = false;

  tipolistas: ITipoLista[] = [];

  editForm = this.fb.group({
    id: [],
    nombre: [null, [Validators.required]],
    descripcion: [null, [Validators.maxLength(500)]],
    padreId: []
  });

  constructor(protected tipoListaService: TipoListaService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tipoLista }) => {
      this.updateForm(tipoLista);

      this.tipoListaService
        .query()
        .pipe(
          map((res: HttpResponse<ITipoLista[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: ITipoLista[]) => (this.tipolistas = resBody));
    });
  }

  updateForm(tipoLista: ITipoLista): void {
    this.editForm.patchValue({
      id: tipoLista.id,
      nombre: tipoLista.nombre,
      descripcion: tipoLista.descripcion,
      padreId: tipoLista.padreId
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tipoLista = this.createFromForm();
    if (tipoLista.id !== undefined) {
      this.subscribeToSaveResponse(this.tipoListaService.update(tipoLista));
    } else {
      this.subscribeToSaveResponse(this.tipoListaService.create(tipoLista));
    }
  }

  private createFromForm(): ITipoLista {
    return {
      ...new TipoLista(),
      id: this.editForm.get(['id'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      descripcion: this.editForm.get(['descripcion'])!.value,
      padreId: this.editForm.get(['padreId'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITipoLista>>): void {
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

  trackById(index: number, item: ITipoLista): any {
    return item.id;
  }
}
