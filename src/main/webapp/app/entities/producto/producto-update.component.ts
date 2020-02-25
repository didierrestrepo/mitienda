import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { IProducto, Producto } from 'app/shared/model/producto.model';
import { ProductoService } from './producto.service';
import { IProductoCategoria } from 'app/shared/model/producto-categoria.model';
import { ProductoCategoriaService } from 'app/entities/producto-categoria/producto-categoria.service';

@Component({
  selector: 'jhi-producto-update',
  templateUrl: './producto-update.component.html'
})
export class ProductoUpdateComponent implements OnInit {
  isSaving = false;

  productocategorias: IProductoCategoria[] = [];

  editForm = this.fb.group({
    id: [],
    nombre: [null, [Validators.required]],
    descripcion: [],
    precioCompra: [null, [Validators.required, Validators.min(0)]],
    precioVenta: [null, [Validators.required, Validators.min(0)]],
    estado: [],
    productCategoriaId: []
  });

  constructor(
    protected productoService: ProductoService,
    protected productoCategoriaService: ProductoCategoriaService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ producto }) => {
      this.updateForm(producto);

      this.productoCategoriaService
        .query()
        .pipe(
          map((res: HttpResponse<IProductoCategoria[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IProductoCategoria[]) => (this.productocategorias = resBody));
    });
  }

  updateForm(producto: IProducto): void {
    this.editForm.patchValue({
      id: producto.id,
      nombre: producto.nombre,
      descripcion: producto.descripcion,
      precioCompra: producto.precioCompra,
      precioVenta: producto.precioVenta,
      estado: producto.estado,
      productCategoriaId: producto.productCategoriaId
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const producto = this.createFromForm();
    if (producto.id !== undefined) {
      this.subscribeToSaveResponse(this.productoService.update(producto));
    } else {
      this.subscribeToSaveResponse(this.productoService.create(producto));
    }
  }

  private createFromForm(): IProducto {
    return {
      ...new Producto(),
      id: this.editForm.get(['id'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      descripcion: this.editForm.get(['descripcion'])!.value,
      precioCompra: this.editForm.get(['precioCompra'])!.value,
      precioVenta: this.editForm.get(['precioVenta'])!.value,
      estado: this.editForm.get(['estado'])!.value,
      productCategoriaId: this.editForm.get(['productCategoriaId'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProducto>>): void {
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

  trackById(index: number, item: IProductoCategoria): any {
    return item.id;
  }
}
