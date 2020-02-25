import { IProductoDetalle } from 'app/shared/model/producto-detalle.model';
import { EstadoProducto } from 'app/shared/model/enumerations/estado-producto.model';

export interface IProducto {
  id?: number;
  nombre?: string;
  descripcion?: string;
  precioCompra?: number;
  precioVenta?: number;
  estado?: EstadoProducto;
  productoDetalles?: IProductoDetalle[];
  productCategoriaId?: number;
}

export class Producto implements IProducto {
  constructor(
    public id?: number,
    public nombre?: string,
    public descripcion?: string,
    public precioCompra?: number,
    public precioVenta?: number,
    public estado?: EstadoProducto,
    public productoDetalles?: IProductoDetalle[],
    public productCategoriaId?: number
  ) {}
}
