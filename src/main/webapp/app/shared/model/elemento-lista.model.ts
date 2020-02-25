export interface IElementoLista {
  id?: number;
  nombre?: string;
  codigo?: string;
  propiedadesContentType?: string;
  propiedades?: any;
  tipoListaNombre?: string;
  tipoListaId?: number;
  padreNombre?: string;
  padreId?: number;
}

export class ElementoLista implements IElementoLista {
  constructor(
    public id?: number,
    public nombre?: string,
    public codigo?: string,
    public propiedadesContentType?: string,
    public propiedades?: any,
    public tipoListaNombre?: string,
    public tipoListaId?: number,
    public padreNombre?: string,
    public padreId?: number
  ) {}
}
