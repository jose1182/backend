import dayjs from 'dayjs/esm';
import { IUsuario } from 'app/entities/usuario/usuario.model';
import { ICategoria } from 'app/entities/categoria/categoria.model';
import { Disponibilidad } from 'app/entities/enumerations/disponibilidad.model';

export interface IServicio {
  id?: number;
  titulo?: string;
  descripcion?: string;
  disponibilidad?: Disponibilidad;
  preciohora?: number;
  preciotraslado?: number;
  fechacreacion?: dayjs.Dayjs;
  fechaactualizacion?: dayjs.Dayjs;
  usuario?: IUsuario | null;
  categorias?: ICategoria[] | null;
}

export class Servicio implements IServicio {
  constructor(
    public id?: number,
    public titulo?: string,
    public descripcion?: string,
    public disponibilidad?: Disponibilidad,
    public preciohora?: number,
    public preciotraslado?: number,
    public fechacreacion?: dayjs.Dayjs,
    public fechaactualizacion?: dayjs.Dayjs,
    public usuario?: IUsuario | null,
    public categorias?: ICategoria[] | null
  ) {}
}

export function getServicioIdentifier(servicio: IServicio): number | undefined {
  return servicio.id;
}
