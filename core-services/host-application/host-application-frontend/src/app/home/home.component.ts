
   
import { Component, OnInit } from '@angular/core';
import { PluginsService } from '../plugins.service';
import { SettingsService } from '../settings.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  constructor(private pluginService: PluginsService, private settingsService: SettingsService) { }

  ngOnInit(): void {
  }

  isPluginLoadedForTile(index) {
    let loaded_bundles = JSON.parse(localStorage.getItem("loaded_bundles"));
    let plugin = loaded_bundles[index];
    if(plugin === undefined) {
      return false;
    }
    return true;
  }

  getPluginName(index) {
    let loaded_bundles = JSON.parse(localStorage.getItem("loaded_bundles"));
    let plugin = loaded_bundles[index];
    return `app-${plugin.name}`
  }

  getPluginLoadUrl(index) {
    let loaded_bundles = JSON.parse(localStorage.getItem("loaded_bundles"));
    let plugin = loaded_bundles[index];
    return this.pluginService.getPluginLoadingUrl(plugin)

  }

  arePluginsLoaded(){
    let loaded_bundles = JSON.parse(localStorage.getItem("loaded_bundles"));
    const hasKeys = !!Object.keys(loaded_bundles).length;
    return hasKeys;
  }

  getNumberOfColumns() {
    return this.settingsService.getNumberOfColumns();
  }

  getNumberOfRows() {
    return this.settingsService.getNumberOfRows();
  }
}