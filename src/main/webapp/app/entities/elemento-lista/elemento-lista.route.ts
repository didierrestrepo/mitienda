import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IElementoLista, ElementoLista } from 'app/shared/model/elemento-lista.model';
import { ElementoListaService } from './elemento-lista.service';
import { ElementoListaComponent } from './elemento-lista.component';
import { ElementoListaDetailComponent } from './elemento-lista-detail.component';
import { ElementoListaUpdateComponent } from './elemento-lista-update.component';

@Injectable({ providedIn: 'root' })
export class ElementoListaResolve implements Resolve<IElementoLista> {
  constructor(private service: ElementoListaService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IElementoLista> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((elementoLista: HttpResponse<ElementoLista>) => {
          if (elementoLista.body) {
            return of(elementoLista.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ElementoLista());
  }
}

export const elementoListaRoute: Routes = [
  {
    path: '',
    component: ElementoListaComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'mitiendaApp.elementoLista.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ElementoListaDetailComponent,
    resolve: {
      elementoLista: ElementoListaResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'mitiendaApp.elementoLista.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ElementoListaUpdateComponent,
    resolve: {
      elementoLista: ElementoListaResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'mitiendaApp.elementoLista.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ElementoListaUpdateComponent,
    resolve: {
      elementoLista: ElementoListaResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'mitiendaApp.elementoLista.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
