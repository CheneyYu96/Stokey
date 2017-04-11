package org.xeon.stockey.ui.stockui;

import java.io.IOException;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.xeon.stockey.ui.main.MainApp;

import javafx.scene.Parent;
import org.xeon.stockey.ui.stockui.market.StockMarketController;
import org.xeon.stockey.ui.stockui.selection.StockListController;
import org.xeon.stockey.ui.stockui.strategy.StockStrategyController;

/** 
 * @author ymc 
 * @version 创建时间：2016年3月9日 下午7:33:25 
 *
 */
public class GeneralInterface {
	public MainApp mainApp;
	
	protected Parent root;

	private Label market;

	private Label list;

	private Label stratege;

	private Label title;
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;

	}

	/*
		called in initialize ui in stockListController
	 */
	public void setLabel(AnchorPane pane){
		VBox vBox = (VBox)pane.getChildren().get(0);
		market = (Label) vBox.getChildren().get(0);
		list = (Label) vBox.getChildren().get(1);
		stratege = (Label) vBox.getChildren().get(2);
	}

	public void setTitle(Node label){
		if(label instanceof Label){
			title = (Label)label;
		}
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
		root = mainApp.getRoot();

		try {
			pane = StockMarketController.launch(mainApp);
		} catch (IOException e) {
			System.err.println("market fail to open");
			e.printStackTrace();
		}

		if(root instanceof BorderPane){
			((BorderPane) root).setCenter(pane);
		}
		clearAll();
		market.getStyleClass().addAll("label-market-selected");
		list.getStyleClass().addAll("label-selection","special2");
		stratege.getStyleClass().addAll("label-schemes","special3");
		title.getStyleClass().add("label-market");

	}
	
	public void jumpToSelection() {
		Parent pane = null;
		root = mainApp.getRoot();

		try {
			pane = StockListController.launchSingle(mainApp);
		} catch (IOException e) {
			System.err.println("market fail to open");
			e.printStackTrace();
		}
		if(root instanceof BorderPane){
			((BorderPane) root).setCenter(pane);
		}

		clearAll();
		market.getStyleClass().addAll("label-market","special1");
		list.getStyleClass().addAll("label-selection-selected");
		stratege.getStyleClass().addAll("label-schemes","special3");
		title.getStyleClass().add("label-selection");
	}
	
	public void jumpToStrategy() {
		Parent pane = null;
		root = mainApp.getRoot();
		try {
			pane = StockStrategyController.launch(mainApp);
		} catch (IOException e) {
			System.err.println("strategy fail to open");
			e.printStackTrace();
		}
		if(root instanceof BorderPane){
			((BorderPane) root).setCenter(pane);
		}

		clearAll();
		market.getStyleClass().addAll("label-market","special1");
		list.getStyleClass().addAll("label-selection","special2");
		stratege.getStyleClass().addAll("label-schemes-selected");
		title.getStyleClass().add("label-schemes");

	}

	public void clearAll(){
		market.getStyleClass().clear();
		list.getStyleClass().clear();
		stratege.getStyleClass().clear();
		title.getStyleClass().clear();
	}

}
