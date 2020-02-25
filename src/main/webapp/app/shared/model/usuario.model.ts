import { Genero } from 'app/shared/model/enumerations/genero.model';

export interface IUsuario {
  id?: number;
  segundoNombre?: string;
  segundoApellido?: string;
  numDocumento?: string;
  celular?: string;
  direccion?: string;
  genero?: Genero;
  userLogin?: string;
  userId?: number;
  cuidadNombre?: string;
  cuidadId?: number;
}

export class Usuario implements IUsuario {
  constructor(
    public id?: number,
    public segundoNombre?: string,
    public segundoApellido?: string,
    public numDocumento?: string,
    public celular?: string,
    public direccion?: string,
    public genero?: Genero,
    public userLogin?: string,
    public userId?: number,
    public cuidadNombre?: string,
    public cuidadId?: number
  ) {}
}
