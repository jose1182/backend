import { IUsuario } from 'app/entities/usuario/usuario.model';

export interface IConversacion {
  id?: number;
  usuarios?: IUsuario[] | null;
}

export class Conversacion implements IConversacion {
  constructor(public id?: number, public usuarios?: IUsuario[] | null) {}
}

export function getConversacionIdentifier(conversacion: IConversacion): number | undefined {
  return conversacion.id;
}
