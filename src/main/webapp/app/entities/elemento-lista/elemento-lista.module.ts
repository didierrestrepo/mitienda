import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MitiendaSharedModule } from 'app/shared/shared.module';
import { ElementoListaComponent } from './elemento-lista.component';
import { ElementoListaDetailComponent } from './elemento-lista-detail.component';
import { ElementoListaUpdateComponent } from './elemento-lista-update.component';
import { ElementoListaDeleteDialogComponent } from './elemento-lista-delete-dialog.component';
import { elementoListaRoute } from './elemento-lista.route';

@NgModule({
  imports: [MitiendaSharedModule, RouterModule.forChild(elementoListaRoute)],
  declarations: [ElementoListaComponent, ElementoListaDetailComponent, ElementoListaUpdateComponent, ElementoListaDeleteDialogComponent],
  entryComponents: [ElementoListaDeleteDialogComponent]
})
export class MitiendaElementoListaModule {}
