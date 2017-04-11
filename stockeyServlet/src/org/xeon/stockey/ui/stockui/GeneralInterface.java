package org.xeon.stockey.ui.stockui;

import java.io.IOException;

import org.xeon.stockey.ui.main.MainApp;

import javafx.scene.Parent;
import javafx.scene.Scene;

/** 
 * @author ymc 
 * @version 创建时间：2016年3月9日 下午7:33:25 
 *
 */
public class GeneralInterface {
	public MainApp mainApp;
	
	private Scene scene;
	
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;

	}
	
	public void closeStage() {
	        mainApp.getPrimaryStage().close();
	        System.exit(0);
    }

	public void minimizeStage() {
    	 mainApp.getPrimaryStage().toBack();
    }
	
	/*
	 * 导航跳转
	 * 后续可增加
	 */
	public void jumpToMarket() {
		Parent pane = null;
		try {
			pane = StockMarketController.launch(mainApp);
		} catch (IOException e) {
			System.err.println("market fail to open");
			e.printStackTrace();
		}
		scene = new Scene(pane);
		mainApp.showOther(scene);
	}
	
	public void jumpToSelection() {
		Parent pane = null;
		try {
			pane = StockListController.launch(mainApp);
		} catch (IOException e) {
			System.err.println("market fail to open");
			e.printStackTrace();
		}
		scene = new Scene(pane);
		mainApp.showOther(scene);
	}
	
	public void jumpToStrategy() {
		
	}
	
	
}
