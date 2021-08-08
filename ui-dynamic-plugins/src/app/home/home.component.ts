import { Component, OnInit } from '@angular/core';
import { PluginsService } from '../plugins.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  public NR_COLUMNS = 2;

  constructor(private pluginService: PluginsService) { }

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
    return plugin.name
  }

  getPluginLoadUrl(index) {
    let loaded_bundles = JSON.parse(localStorage.getItem("loaded_bundles"));
    let plugin = loaded_bundles[index];
    return this.pluginService.getPluginLoadingUrl(plugin)

  }

}
