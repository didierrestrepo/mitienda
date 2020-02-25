import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ITipoLista } from 'app/shared/model/tipo-lista.model';

type EntityResponseType = HttpResponse<ITipoLista>;
type EntityArrayResponseType = HttpResponse<ITipoLista[]>;

@Injectable({ providedIn: 'root' })
export class TipoListaService {
  public resourceUrl = SERVER_API_URL + 'api/tipo-listas';

  constructor(protected http: HttpClient) {}

  create(tipoLista: ITipoLista): Observable<EntityResponseType> {
    return this.http.post<ITipoLista>(this.resourceUrl, tipoLista, { observe: 'response' });
  }

  update(tipoLista: ITipoLista): Observable<EntityResponseType> {
    return this.http.put<ITipoLista>(this.resourceUrl, tipoLista, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITipoLista>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITipoLista[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
