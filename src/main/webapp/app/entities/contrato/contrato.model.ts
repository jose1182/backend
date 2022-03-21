import dayjs from 'dayjs/esm';
import { IUsuario } from 'app/entities/usuario/usuario.model';
import { IServicio } from 'app/entities/servicio/servicio.model';

export interface IContrato {
  id?: number;
  preciofinal?: number;
  fecha?: dayjs.Dayjs;
  usuario?: IUsuario | null;
  servicio?: IServicio | null;
}

export class Contrato implements IContrato {
  constructor(
    public id?: number,
    public preciofinal?: number,
    public fecha?: dayjs.Dayjs,
    public usuario?: IUsuario | null,
    public servicio?: IServicio | null
  ) {}
}

export function getContratoIdentifier(contrato: IContrato): number | undefined {
  return contrato.id;
}
