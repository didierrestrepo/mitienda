export interface IProductoDetalle {
  id?: number;
  codigo?: string;
  tallaValor?: string;
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
    public tallaValor?: string,
    public tallaId?: number,
    public colorNombre?: string,
    public colorId?: number,
    public productoNombre?: string,
    public productoId?: number
  ) {}
}
