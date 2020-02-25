import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ITipoLista } from 'app/shared/model/tipo-lista.model';
import { TipoListaService } from './tipo-lista.service';

@Component({
  templateUrl: './tipo-lista-delete-dialog.component.html'
})
export class TipoListaDeleteDialogComponent {
  tipoLista?: ITipoLista;

  constructor(protected tipoListaService: TipoListaService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.tipoListaService.delete(id).subscribe(() => {
      this.eventManager.broadcast('tipoListaListModification');
      this.activeModal.close();
    });
  }
}
