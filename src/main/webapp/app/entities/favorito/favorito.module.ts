import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { FavoritoComponent } from './list/favorito.component';
import { FavoritoDetailComponent } from './detail/favorito-detail.component';
import { FavoritoUpdateComponent } from './update/favorito-update.component';
import { FavoritoDeleteDialogComponent } from './delete/favorito-delete-dialog.component';
import { FavoritoRoutingModule } from './route/favorito-routing.module';

@NgModule({
  imports: [SharedModule, FavoritoRoutingModule],
  declarations: [FavoritoComponent, FavoritoDetailComponent, FavoritoUpdateComponent, FavoritoDeleteDialogComponent],
  entryComponents: [FavoritoDeleteDialogComponent],
})
export class FavoritoModule {}
