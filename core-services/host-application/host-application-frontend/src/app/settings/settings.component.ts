import { Component, OnInit } from '@angular/core';
import { SettingsService } from '../settings.service';

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.css']
})
export class SettingsComponent implements OnInit {

  constructor(private settingsService: SettingsService) { }

  public numberOfColumns = [0,1,2,3,4,5,6];
  public numberOfRows = [1,2,3,4,5,6];
  public nrColumnsSelectedOption = this.getNumberOfColumns();
  public nrRowsSelectedOption = this.getNumberOfRows();

  ngOnInit(): void {
  }

  onSettingsSaved() {
    this.settingsService.setNrColumns(this.nrColumnsSelectedOption)
    this.settingsService.setNrRows(this.nrRowsSelectedOption)
  }

  getNumberOfColumns() {
    return this.settingsService.getNumberOfColumns();
  }

  getNumberOfRows() {
    return this.settingsService.getNumberOfRows();
  }
}
