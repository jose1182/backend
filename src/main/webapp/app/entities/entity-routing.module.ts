import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'valoracion',
        data: { pageTitle: 'Valoracions' },
        loadChildren: () => import('./valoracion/valoracion.module').then(m => m.ValoracionModule),
      },
      {
        path: 'usuario',
        data: { pageTitle: 'Usuarios' },
        loadChildren: () => import('./usuario/usuario.module').then(m => m.UsuarioModule),
      },
      {
        path: 'servicio',
        data: { pageTitle: 'Servicios' },
        loadChildren: () => import('./servicio/servicio.module').then(m => m.ServicioModule),
      },
      {
        path: 'categoria',
        data: { pageTitle: 'Categorias' },
        loadChildren: () => import('./categoria/categoria.module').then(m => m.CategoriaModule),
      },
      {
        path: 'contrato',
        data: { pageTitle: 'Contratoes' },
        loadChildren: () => import('./contrato/contrato.module').then(m => m.ContratoModule),
      },
      {
        path: 'favorito',
        data: { pageTitle: 'Favoritos' },
        loadChildren: () => import('./favorito/favorito.module').then(m => m.FavoritoModule),
      },
      {
        path: 'mensaje',
        data: { pageTitle: 'Mensajes' },
        loadChildren: () => import('./mensaje/mensaje.module').then(m => m.MensajeModule),
      },
      {
        path: 'conversacion',
        data: { pageTitle: 'Conversacions' },
        loadChildren: () => import('./conversacion/conversacion.module').then(m => m.ConversacionModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
