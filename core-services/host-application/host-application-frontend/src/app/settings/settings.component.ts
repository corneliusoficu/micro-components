import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.css']
})
export class SettingsComponent implements OnInit {

  constructor() { }

  public numberOfColumns = [0,1,2,3,4,5,6];
  public numberOfRows = [1,2,3,4,5,6];
  public nrColumnsSelectedOption = 3;
  public nrRowsSelectedOption=1;

  ngOnInit(): void {
  }

}
