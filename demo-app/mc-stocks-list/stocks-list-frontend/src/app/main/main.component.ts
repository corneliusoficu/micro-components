import { Component, OnInit, ViewEncapsulation } from '@angular/core';

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class MainComponent implements OnInit {
  portfolioStocks = [
    {
      name: 'Microsoft',
      exchange: 'Nasdaq',
      shares: '245.9',
      totalValue: '81,021'
    },
    {
      name: 'AirBnb',
      exchange: 'Nasdaq',
      shares: '81',
      totalValue: '14,501'
    },
    {
      name: 'Alibaba',
      exchange: 'NYSE',
      shares: '1055',
      totalValue: '7,000'
    },
    {
      name: 'Exxon',
      exchange: 'NYSE',
      shares: '67',
      totalValue: '32,000'
    }
  ]
  constructor() { }

  ngOnInit(): void {
  }

}