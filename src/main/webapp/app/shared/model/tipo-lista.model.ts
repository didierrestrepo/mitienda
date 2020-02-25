export interface ITipoLista {
  id?: number;
  nombre?: string;
  descripcion?: string;
  padreNombre?: string;
  padreId?: number;
}

export class TipoLista implements ITipoLista {
  constructor(
    public id?: number,
    public nombre?: string,
    public descripcion?: string,
    public padreNombre?: string,
    public padreId?: number
  ) {}
}
