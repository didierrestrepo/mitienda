import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MitiendaSharedModule } from 'app/shared/shared.module';
import { ProductoDetalleComponent } from './producto-detalle.component';
import { ProductoDetalleDetailComponent } from './producto-detalle-detail.component';
import { ProductoDetalleUpdateComponent } from './producto-detalle-update.component';
import { ProductoDetalleDeleteDialogComponent } from './producto-detalle-delete-dialog.component';
import { productoDetalleRoute } from './producto-detalle.route';

@NgModule({
  imports: [MitiendaSharedModule, RouterModule.forChild(productoDetalleRoute)],
  declarations: [
    ProductoDetalleComponent,
    ProductoDetalleDetailComponent,
    ProductoDetalleUpdateComponent,
    ProductoDetalleDeleteDialogComponent
  ],
  entryComponents: [ProductoDetalleDeleteDialogComponent]
})
export class MitiendaProductoDetalleModule {}
