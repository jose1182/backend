import { IServicio } from 'app/entities/servicio/servicio.model';

export interface ICategoria {
  id?: number;
  nombre?: string;
  servicios?: IServicio[] | null;
}

export class Categoria implements ICategoria {
  constructor(public id?: number, public nombre?: string, public servicios?: IServicio[] | null) {}
}

export function getCategoriaIdentifier(categoria: ICategoria): number | undefined {
  return categoria.id;
}
