import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IElementoLista } from 'app/shared/model/elemento-lista.model';

type EntityResponseType = HttpResponse<IElementoLista>;
type EntityArrayResponseType = HttpResponse<IElementoLista[]>;

@Injectable({ providedIn: 'root' })
export class ElementoListaService {
  public resourceUrl = SERVER_API_URL + 'api/elemento-listas';

  constructor(protected http: HttpClient) {}

  create(elementoLista: IElementoLista): Observable<EntityResponseType> {
    return this.http.post<IElementoLista>(this.resourceUrl, elementoLista, { observe: 'response' });
  }

  update(elementoLista: IElementoLista): Observable<EntityResponseType> {
    return this.http.put<IElementoLista>(this.resourceUrl, elementoLista, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IElementoLista>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IElementoLista[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
