import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { IElementoLista } from 'app/shared/model/elemento-lista.model';

@Component({
  selector: 'jhi-elemento-lista-detail',
  templateUrl: './elemento-lista-detail.component.html'
})
export class ElementoListaDetailComponent implements OnInit {
  elementoLista: IElementoLista | null = null;

  constructor(protected dataUtils: JhiDataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ elementoLista }) => {
      this.elementoLista = elementoLista;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType: string, base64String: string): void {
    this.dataUtils.openFile(contentType, base64String);
  }

  previousState(): void {
    window.history.back();
  }
}
