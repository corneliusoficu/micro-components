import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class SettingsService {

  DEFAULT_NR_COLS = 1
  DEFAULT_NR_ROWS = 1

  setNrColumns(nrColumns) {
    let layout = JSON.parse(localStorage.getItem("layout"));
    if(layout == null) {
      layout = {rows: this.DEFAULT_NR_ROWS, columns: this.DEFAULT_NR_COLS}
    }
    layout.columns = nrColumns
    localStorage.setItem("layout", JSON.stringify(layout));
  }

  setNrRows(nrRows) {
    let layout = JSON.parse(localStorage.getItem("layout"));
    if(layout == null) {
      layout = {rows: this.DEFAULT_NR_ROWS, columns: this.DEFAULT_NR_COLS}
    }
    layout.rows = nrRows
    localStorage.setItem("layout", JSON.stringify(layout));
  }

  getNumberOfColumns() {
    let layout = JSON.parse(localStorage.getItem("layout"));
    if(layout == null) {
      return this.DEFAULT_NR_COLS;
    }
    return layout.columns;
  }

  getNumberOfRows() {
    let layout = JSON.parse(localStorage.getItem("layout"));
    if(layout == null) {
      return this.DEFAULT_NR_ROWS
    }
    return layout.rows;
  }

  constructor() { }
}
