import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ITipoLista, TipoLista } from 'app/shared/model/tipo-lista.model';
import { TipoListaService } from './tipo-lista.service';
import { TipoListaComponent } from './tipo-lista.component';
import { TipoListaDetailComponent } from './tipo-lista-detail.component';
import { TipoListaUpdateComponent } from './tipo-lista-update.component';

@Injectable({ providedIn: 'root' })
export class TipoListaResolve implements Resolve<ITipoLista> {
  constructor(private service: TipoListaService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITipoLista> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((tipoLista: HttpResponse<TipoLista>) => {
          if (tipoLista.body) {
            return of(tipoLista.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new TipoLista());
  }
}

export const tipoListaRoute: Routes = [
  {
    path: '',
    component: TipoListaComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'mitiendaApp.tipoLista.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: TipoListaDetailComponent,
    resolve: {
      tipoLista: TipoListaResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'mitiendaApp.tipoLista.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: TipoListaUpdateComponent,
    resolve: {
      tipoLista: TipoListaResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'mitiendaApp.tipoLista.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: TipoListaUpdateComponent,
    resolve: {
      tipoLista: TipoListaResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'mitiendaApp.tipoLista.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
