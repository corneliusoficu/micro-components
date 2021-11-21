import { Injectable, OnDestroy } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { catchError, retry, switchMap, share, takeUntil, filter, take, timeout, takeWhile, map, tap } from 'rxjs/operators';
import { HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError, timer, Subject, interval, of } from 'rxjs';

export interface MicroComponentsResponse {
  micro_components: MicroComponent[]
}

export interface MicroComponent {
  name: string;
  location: string;
}

@Injectable({
  providedIn: 'root'
})
export class PluginsService implements OnDestroy {

  pluginsStoreBaseEndpoint = "http://localhost:4200/cxf"
  pluginsHandlerBaseEndpoint = "http://localhost:4200/cxf"

  public stopPolling = new Subject();

  constructor(private http: HttpClient) { } 

  getAvailablePlugins() {
    return this.http.get<MicroComponentsResponse>(this.pluginsStoreBaseEndpoint + "/lifecycle-handler/micro-components");
  }

  getPluginHealth(microComponent: MicroComponent) {
    return this.http.get(`${this.pluginsHandlerBaseEndpoint}/${microComponent.name}/health`).pipe(
      catchError(e => of({status: e.status}))
    )
  }

  installPlugin(microComponent: MicroComponent) {
    console.log("Executing post to install plugin!");
    return of({"response": "success"})
  }

  pollPluginInstalled(microComponent: MicroComponent) {
    return timer(1, 1000).pipe(
      switchMap(() => this.getPluginHealth(microComponent)),
      filter((r:any) => r.status == 200),
      take(1),
      timeout(30000)
    );
  }

  getPluginLoadingUrl(microComponent: MicroComponent) {
    return `${this.pluginsHandlerBaseEndpoint}/${microComponent.name}/view`
  }

  ngOnDestroy() {
    this.stopPolling.next();
  }

  private handleError(error: HttpErrorResponse) {
    if (error.status === 0) {
      // A client-side or network error occurred. Handle it accordingly.
      console.error('An error occurred:', error.error);
    } else {
      // The backend returned an unsuccessful response code.
      // The response body may contain clues as to what went wrong.
      console.error(
        `Backend returned code ${error.status}, body was: `, error.error);
    }
    // Return an observable with a user-facing error message.
    return throwError(
      'Something bad happened; please try again later.');
  }
}