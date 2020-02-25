import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { IProductoDetalle, ProductoDetalle } from 'app/shared/model/producto-detalle.model';
import { ProductoDetalleService } from './producto-detalle.service';
import { IElementoLista } from 'app/shared/model/elemento-lista.model';
import { ElementoListaService } from 'app/entities/elemento-lista/elemento-lista.service';
import { IProducto } from 'app/shared/model/producto.model';
import { ProductoService } from 'app/entities/producto/producto.service';

type SelectableEntity = IElementoLista | IProducto;

@Component({
  selector: 'jhi-producto-detalle-update',
  templateUrl: './producto-detalle-update.component.html'
})
export class ProductoDetalleUpdateComponent implements OnInit {
  isSaving = false;

  elementolistas: IElementoLista[] = [];

  productos: IProducto[] = [];

  editForm = this.fb.group({
    id: [],
    codigo: [],
    tallaId: [null, Validators.required],
    colorId: [null, Validators.required],
    productoId: []
  });

  constructor(
    protected productoDetalleService: ProductoDetalleService,
    protected elementoListaService: ElementoListaService,
    protected productoService: ProductoService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ productoDetalle }) => {
      this.updateForm(productoDetalle);

      this.elementoListaService
        .query()
        .pipe(
          map((res: HttpResponse<IElementoLista[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IElementoLista[]) => (this.elementolistas = resBody));

      this.productoService
        .query()
        .pipe(
          map((res: HttpResponse<IProducto[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IProducto[]) => (this.productos = resBody));
    });
  }

  updateForm(productoDetalle: IProductoDetalle): void {
    this.editForm.patchValue({
      id: productoDetalle.id,
      codigo: productoDetalle.codigo,
      tallaId: productoDetalle.tallaId,
      colorId: productoDetalle.colorId,
      productoId: productoDetalle.productoId
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const productoDetalle = this.createFromForm();
    if (productoDetalle.id !== undefined) {
      this.subscribeToSaveResponse(this.productoDetalleService.update(productoDetalle));
    } else {
      this.subscribeToSaveResponse(this.productoDetalleService.create(productoDetalle));
    }
  }

  private createFromForm(): IProductoDetalle {
    return {
      ...new ProductoDetalle(),
      id: this.editForm.get(['id'])!.value,
      codigo: this.editForm.get(['codigo'])!.value,
      tallaId: this.editForm.get(['tallaId'])!.value,
      colorId: this.editForm.get(['colorId'])!.value,
      productoId: this.editForm.get(['productoId'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProductoDetalle>>): void {
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
