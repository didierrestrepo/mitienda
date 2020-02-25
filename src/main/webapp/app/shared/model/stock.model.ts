export interface IStock {
  id?: number;
  cantidad?: number;
  productoId?: number;
  sucursalId?: number;
}

export class Stock implements IStock {
  constructor(public id?: number, public cantidad?: number, public productoId?: number, public sucursalId?: number) {}
}
