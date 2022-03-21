import dayjs from 'dayjs/esm';
import { IUsuario } from 'app/entities/usuario/usuario.model';
import { IConversacion } from 'app/entities/conversacion/conversacion.model';

export interface IMensaje {
  id?: number;
  texto?: string;
  fecha?: dayjs.Dayjs;
  emisor?: IUsuario | null;
  receptor?: IUsuario | null;
  conversacion?: IConversacion | null;
}

export class Mensaje implements IMensaje {
  constructor(
    public id?: number,
    public texto?: string,
    public fecha?: dayjs.Dayjs,
    public emisor?: IUsuario | null,
    public receptor?: IUsuario | null,
    public conversacion?: IConversacion | null
  ) {}
}

export function getMensajeIdentifier(mensaje: IMensaje): number | undefined {
  return mensaje.id;
}
