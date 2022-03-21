import { IUsuario } from 'app/entities/usuario/usuario.model';
import { IServicio } from 'app/entities/servicio/servicio.model';

export interface IFavorito {
  id?: number;
  usuario?: IUsuario | null;
  servicio?: IServicio | null;
}

export class Favorito implements IFavorito {
  constructor(public id?: number, public usuario?: IUsuario | null, public servicio?: IServicio | null) {}
}

export function getFavoritoIdentifier(favorito: IFavorito): number | undefined {
  return favorito.id;
}
