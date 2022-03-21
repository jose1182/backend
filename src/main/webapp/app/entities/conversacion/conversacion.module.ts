import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ConversacionComponent } from './list/conversacion.component';
import { ConversacionDetailComponent } from './detail/conversacion-detail.component';
import { ConversacionUpdateComponent } from './update/conversacion-update.component';
import { ConversacionDeleteDialogComponent } from './delete/conversacion-delete-dialog.component';
import { ConversacionRoutingModule } from './route/conversacion-routing.module';

@NgModule({
  imports: [SharedModule, ConversacionRoutingModule],
  declarations: [ConversacionComponent, ConversacionDetailComponent, ConversacionUpdateComponent, ConversacionDeleteDialogComponent],
  entryComponents: [ConversacionDeleteDialogComponent],
})
export class ConversacionModule {}
