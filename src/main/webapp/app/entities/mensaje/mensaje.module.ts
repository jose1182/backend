import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { MensajeComponent } from './list/mensaje.component';
import { MensajeDetailComponent } from './detail/mensaje-detail.component';
import { MensajeUpdateComponent } from './update/mensaje-update.component';
import { MensajeDeleteDialogComponent } from './delete/mensaje-delete-dialog.component';
import { MensajeRoutingModule } from './route/mensaje-routing.module';

@NgModule({
  imports: [SharedModule, MensajeRoutingModule],
  declarations: [MensajeComponent, MensajeDetailComponent, MensajeUpdateComponent, MensajeDeleteDialogComponent],
  entryComponents: [MensajeDeleteDialogComponent],
})
export class MensajeModule {}
