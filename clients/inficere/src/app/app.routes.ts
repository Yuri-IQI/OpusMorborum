import { Routes } from '@angular/router';
import { StartPageComponent } from './features/pages/start-page/start-page.component';
import { ApothecaryPageComponent } from './features/pages/apothecary-page/apothecary-page.component';

export const routes: Routes = [
  { path: '', component: StartPageComponent },
  { path: 'apothecary', component: ApothecaryPageComponent }
];
