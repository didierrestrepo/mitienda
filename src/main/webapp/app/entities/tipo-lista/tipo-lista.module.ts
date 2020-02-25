import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MitiendaSharedModule } from 'app/shared/shared.module';
import { TipoListaComponent } from './tipo-lista.component';
import { TipoListaDetailComponent } from './tipo-lista-detail.component';
import { TipoListaUpdateComponent } from './tipo-lista-update.component';
import { TipoListaDeleteDialogComponent } from './tipo-lista-delete-dialog.component';
import { tipoListaRoute } from './tipo-lista.route';

@NgModule({
  imports: [MitiendaSharedModule, RouterModule.forChild(tipoListaRoute)],
  declarations: [TipoListaComponent, TipoListaDetailComponent, TipoListaUpdateComponent, TipoListaDeleteDialogComponent],
  entryComponents: [TipoListaDeleteDialogComponent]
})
export class MitiendaTipoListaModule {}
