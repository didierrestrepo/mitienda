export interface IProductoDetalle {
  id?: number;
  codigo?: string;
  tallaNombre?: string;
  tallaId?: number;
  colorNombre?: string;
  colorId?: number;
  productoNombre?: string;
  productoId?: number;
}

export class ProductoDetalle implements IProductoDetalle {
  constructor(
    public id?: number,
    public codigo?: string,
    public tallaNombre?: string,
    public tallaId?: number,
    public colorNombre?: string,
    public colorId?: number,
    public productoNombre?: string,
    public productoId?: number
  ) {}
}
