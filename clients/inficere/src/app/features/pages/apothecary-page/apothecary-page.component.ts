import { Component } from '@angular/core';
import { QuestMenuComponent } from "./components/quest-menu/quest-menu.component";

@Component({
  selector: 'app-apothecary-page',
  imports: [QuestMenuComponent],
  standalone: true,
  templateUrl: './apothecary-page.component.html',
  styleUrl: './apothecary-page.component.css'
})
export class ApothecaryPageComponent {

}
