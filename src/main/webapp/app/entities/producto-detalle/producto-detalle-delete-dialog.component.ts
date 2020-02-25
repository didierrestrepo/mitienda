import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IProductoDetalle } from 'app/shared/model/producto-detalle.model';
import { ProductoDetalleService } from './producto-detalle.service';

@Component({
  templateUrl: './producto-detalle-delete-dialog.component.html'
})
export class ProductoDetalleDeleteDialogComponent {
  productoDetalle?: IProductoDetalle;

  constructor(
    protected productoDetalleService: ProductoDetalleService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.productoDetalleService.delete(id).subscribe(() => {
      this.eventManager.broadcast('productoDetalleListModification');
      this.activeModal.close();
    });
  }
}
