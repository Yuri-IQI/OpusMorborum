import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class EventBusService {
  private _event$ = new Subject<void>();
  public event$ = this._event$.asObservable();

  emitEvent(): void {
    this._event$.next();
  }
}