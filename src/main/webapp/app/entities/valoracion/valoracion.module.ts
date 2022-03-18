import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ValoracionComponent } from './list/valoracion.component';
import { ValoracionDetailComponent } from './detail/valoracion-detail.component';
import { ValoracionUpdateComponent } from './update/valoracion-update.component';
import { ValoracionDeleteDialogComponent } from './delete/valoracion-delete-dialog.component';
import { ValoracionRoutingModule } from './route/valoracion-routing.module';

@NgModule({
  imports: [SharedModule, ValoracionRoutingModule],
  declarations: [ValoracionComponent, ValoracionDetailComponent, ValoracionUpdateComponent, ValoracionDeleteDialogComponent],
  entryComponents: [ValoracionDeleteDialogComponent],
})
export class ValoracionModule {}
