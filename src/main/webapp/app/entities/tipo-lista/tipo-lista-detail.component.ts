import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITipoLista } from 'app/shared/model/tipo-lista.model';

@Component({
  selector: 'jhi-tipo-lista-detail',
  templateUrl: './tipo-lista-detail.component.html'
})
export class TipoListaDetailComponent implements OnInit {
  tipoLista: ITipoLista | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tipoLista }) => {
      this.tipoLista = tipoLista;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
