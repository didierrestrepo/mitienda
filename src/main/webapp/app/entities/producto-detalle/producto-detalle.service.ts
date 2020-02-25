import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IProductoDetalle } from 'app/shared/model/producto-detalle.model';

type EntityResponseType = HttpResponse<IProductoDetalle>;
type EntityArrayResponseType = HttpResponse<IProductoDetalle[]>;

@Injectable({ providedIn: 'root' })
export class ProductoDetalleService {
  public resourceUrl = SERVER_API_URL + 'api/producto-detalles';

  constructor(protected http: HttpClient) {}

  create(productoDetalle: IProductoDetalle): Observable<EntityResponseType> {
    return this.http.post<IProductoDetalle>(this.resourceUrl, productoDetalle, { observe: 'response' });
  }

  update(productoDetalle: IProductoDetalle): Observable<EntityResponseType> {
    return this.http.put<IProductoDetalle>(this.resourceUrl, productoDetalle, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IProductoDetalle>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IProductoDetalle[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
