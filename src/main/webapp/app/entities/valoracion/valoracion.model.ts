import dayjs from 'dayjs/esm';

export interface IValoracion {
  id?: number;
  descripcion?: string;
  fecha?: dayjs.Dayjs;
  id_servicio?: number | null;
}

export class Valoracion implements IValoracion {
  constructor(public id?: number, public descripcion?: string, public fecha?: dayjs.Dayjs, public id_servicio?: number | null) {}
}

export function getValoracionIdentifier(valoracion: IValoracion): number | undefined {
  return valoracion.id;
}
