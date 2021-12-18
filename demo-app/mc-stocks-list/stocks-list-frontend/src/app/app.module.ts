import { BrowserModule } from '@angular/platform-browser';
import { Injector, NgModule } from '@angular/core';
import { createCustomElement } from '@angular/elements';
import { AppComponent } from './app.component';
import { MainComponent } from './main/main.component';
import { MatListModule } from '@angular/material/list';

@NgModule({
  declarations: [
    AppComponent,
     MainComponent
  ],
  imports: [
    BrowserModule,
    MatListModule
  ],
  entryComponents: [MainComponent],
  providers: [],
})
export class AppModule {
  constructor(private injector: Injector) {}

  ngDoBootstrap() {
    console.log("Registering custom element with name: app-stocks-list")
    const customElement = createCustomElement(MainComponent, {injector: this.injector})
    customElements.get('app-stocks-list') || customElements.define('app-stocks-list', customElement)
  }
}