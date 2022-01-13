import { BrowserModule } from '@angular/platform-browser';
import { Injector, NgModule } from '@angular/core';
import { createCustomElement } from '@angular/elements';
import { AppComponent } from './app.component';
import { MainComponent } from './main/main.component';
import { MatInputModule } from '@angular/material/input';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';

import { BrowserAnimationsModule } from '@angular/platform-browser/animations';


@NgModule({
  declarations: [
    AppComponent,
     MainComponent
  ],
  imports: [
    BrowserAnimationsModule,
    BrowserModule,
    MatInputModule,
    FormsModule,
    ReactiveFormsModule,
    MatButtonModule
  ],
  entryComponents: [MainComponent],
  providers: [],
})
export class AppModule {
  constructor(private injector: Injector) {}

  ngDoBootstrap() {
    console.log("Registering custom element with name: app-auth")
    const customElement = createCustomElement(MainComponent, {injector: this.injector})
    customElements.get('app-auth') || customElements.define('app-auth', customElement)
  }
}