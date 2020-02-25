import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IElementoLista } from 'app/shared/model/elemento-lista.model';
import { ElementoListaService } from './elemento-lista.service';

@Component({
  templateUrl: './elemento-lista-delete-dialog.component.html'
})
export class ElementoListaDeleteDialogComponent {
  elementoLista?: IElementoLista;

  constructor(
    protected elementoListaService: ElementoListaService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.elementoListaService.delete(id).subscribe(() => {
      this.eventManager.broadcast('elementoListaListModification');
      this.activeModal.close();
    });
  }
}
