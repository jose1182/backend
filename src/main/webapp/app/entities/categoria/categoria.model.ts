import { IServicio } from 'app/entities/servicio/servicio.model';

export interface ICategoria {
  id?: number;
  nombre?: string;
  imagenContentType?: string | null;
  imagen?: string | null;
  servicios?: IServicio[] | null;
}

export class Categoria implements ICategoria {
  constructor(
    public id?: number,
    public nombre?: string,
    public imagenContentType?: string | null,
    public imagen?: string | null,
    public servicios?: IServicio[] | null
  ) {}
}

export function getCategoriaIdentifier(categoria: ICategoria): number | undefined {
  return categoria.id;
}
