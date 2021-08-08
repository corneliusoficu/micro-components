import { Injectable, OnDestroy } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { catchError, retry, switchMap, share, takeUntil, filter, take, timeout, takeWhile } from 'rxjs/operators';
import { HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError, timer, Subject, interval, of } from 'rxjs';

export interface PluginInfo {
  name: string;
  version: string;
  extension: string;
}

@Injectable({
  providedIn: 'root'
})
export class PluginsService implements OnDestroy {

  pluginsStoreBaseEndpoint = "http://localhost:2000"
  pluginsHandlerBaseEndpoint = "http://localhost:4200/cxf"

  public stopPolling = new Subject();

  constructor(private http: HttpClient) { } 

  getAvailablePlugins() {
    return this.http.get<PluginInfo[]>(this.pluginsStoreBaseEndpoint + "/bundles");
  }

  getPluginHealth(plugin: PluginInfo) {
    return this.http.get(`${this.pluginsHandlerBaseEndpoint}/${plugin.name}/health`).pipe(
      catchError(e => of({status: e.status}))
    )
  }

  installPlugin(plugin: PluginInfo) {
    console.log("Executing post to install plugin!");
    return this.http.post(this.pluginsHandlerBaseEndpoint + "/api/bundles", plugin).pipe(
      catchError(this.handleError)
    );
  }

  pollPluginInstalled(plugin: PluginInfo) {
    return interval(2000).pipe(
      switchMap(() => this.getPluginHealth(plugin)),
      takeWhile((data: any) => data.status != 200),
      timeout(30000)
    );
  }

  getPluginLoadingUrl(plugin: PluginInfo) {
    return `${this.pluginsHandlerBaseEndpoint}/${plugin.name}/view`
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
