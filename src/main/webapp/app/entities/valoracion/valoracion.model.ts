import dayjs from 'dayjs/esm';
import { IUsuario } from 'app/entities/usuario/usuario.model';
import { IServicio } from 'app/entities/servicio/servicio.model';

export interface IValoracion {
  id?: number;
  descripcion?: string;
  fecha?: dayjs.Dayjs;
  puntuacion?: number;
  usuario?: IUsuario | null;
  servicio?: IServicio | null;
}

export class Valoracion implements IValoracion {
  constructor(
    public id?: number,
    public descripcion?: string,
    public fecha?: dayjs.Dayjs,
    public puntuacion?: number,
    public usuario?: IUsuario | null,
    public servicio?: IServicio | null
  ) {}
}

export function getValoracionIdentifier(valoracion: IValoracion): number | undefined {
  return valoracion.id;
}
