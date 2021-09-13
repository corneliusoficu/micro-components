import { Component, OnInit, Input, Inject, OnDestroy } from '@angular/core';
import { PluginsService, PluginInfo } from '../plugins.service';
import {MatDialog, MatDialogRef, MAT_DIALOG_DATA} from '@angular/material/dialog';
import { ThrowStmt } from '@angular/compiler';

export interface DialogData {
  pluginIndex: number;
}

@Component({
  selector: 'app-edit',
  templateUrl: './edit.component.html',
  styleUrls: ['./edit.component.css']
})
export class EditComponent implements OnInit {

  public NR_COLUMNS = 2;

  constructor(public dialog: MatDialog) { }

  ngOnInit(): void {
  }

  onInstallPluginButtonPressed(index) {
    const dialogRef = this.dialog.open(DialogChangePlugin, {
      data: {pluginIndex: index}
    });
  }

  onUninstallPluginButtonPressed(index) {
    let loaded_bundles = JSON.parse(localStorage.getItem("loaded_bundles"));
    if(loaded_bundles == null) {
      return
    }
    delete loaded_bundles[index.toLocaleString()];
    localStorage.setItem("loaded_bundles", JSON.stringify(loaded_bundles));
  }

  getLoadedPluginForIndex(index) {
    let loaded_bundles = JSON.parse(localStorage.getItem("loaded_bundles"));
    let plugin = loaded_bundles[index];
    if(plugin === undefined) {
      return "-"
    }
    return `${plugin.name}-${plugin.version}.${plugin.extension}`;
  }

  doesTileContainPlugin(index) {
    let loaded_bundles = JSON.parse(localStorage.getItem("loaded_bundles"));
    let plugin = loaded_bundles[index];
    if(plugin === undefined) {
      return false;
    }
    return true;
  }
}

@Component({
  selector: 'dialog-change-plugin',
  templateUrl: 'dialog-change-plugin.html',
  styleUrls: ['./dialog-change-plugin.css']
})
export class DialogChangePlugin implements OnDestroy {
  
  pluginInfoList: PluginInfo[] | undefined;
  plugin: PluginInfo | undefined;
  pluginIndex = 0;
  pluginAlreadyInstalled = false;
  loading = true;
  loadingMessage = "Loading...";
  
  selectedBundle = null;


  constructor(
    public dialogRef: MatDialogRef<DialogChangePlugin>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData,
    private pluginService: PluginsService) {
      this.pluginIndex = data.pluginIndex;
      this.pluginService.getAvailablePlugins().subscribe((data: PluginInfo[]) => {
        this.pluginInfoList = data;
        this.handleAlreadySelectedBundle();
        this.loading = false;
      });
  }

  handleAlreadySelectedBundle() {
    let loaded_bundles = JSON.parse(localStorage.getItem("loaded_bundles"));
    
    if(loaded_bundles == null) {
      return
    }

    let loaded_bundle_for_index = loaded_bundles[this.pluginIndex.toLocaleString()];

    if(loaded_bundle_for_index === undefined) {
      return
    }

    let already_loaded_bundle = this.pluginInfoList.filter((plugin,index) => { 
      return plugin.name == loaded_bundle_for_index.name &&
      plugin.version == loaded_bundle_for_index.version && 
      plugin.extension == loaded_bundle_for_index.extension
    })

    if(already_loaded_bundle.length == 0) {
      return
    }

    this.pluginAlreadyInstalled = true;
    this.selectedBundle = already_loaded_bundle
  }

  onPluginInstallClicked() {
    // console.log(tileIndex);
    if(this.selectedBundle == null) {
      return;
    }

    let loaded_bundles = JSON.parse(localStorage.getItem("loaded_bundles"));
    if(loaded_bundles == null) {
      loaded_bundles = {}
    }
    loaded_bundles[this.pluginIndex.toLocaleString()] = this.selectedBundle[0];

    

    this.pluginService.installPlugin(this.selectedBundle[0]).subscribe(response => {
      this.loading = true;
      this.loadingMessage = "Installing plugin..."
      this.pluginService.pollPluginInstalled(this.selectedBundle[0]).subscribe(response => {
        localStorage.setItem("loaded_bundles", JSON.stringify(loaded_bundles));
        this.dialogRef.close();
      })
    })
  }

  ngOnDestroy() {
    this.pluginService.stopPolling.next();
  }
}
