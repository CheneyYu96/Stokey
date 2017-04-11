package org.xeon.stockey.ui.main;

import java.io.IOException;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import org.xeon.stockey.ui.stockui.GeneralController;
import org.xeon.stockey.ui.stockui.selection.StockListController;
import org.xeon.stockey.ui.utility.DragedScene;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * @author ymc,Alan
 * @version Last modified: 2016-3-10 23:55
 *
 */
public class MainApp extends Application  {

	private Stage primaryStage;

	private Parent root;

	public MainApp() {
		
	}

	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public void showOther(Scene scene) {
		this.primaryStage.setScene(scene);
		DragedScene.enableDrag(scene,primaryStage);
		this.primaryStage.show();
	}

	public Parent getRoot() {
		return root;
	}

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		try {
			root = GeneralController.launch(this);
		} catch (IOException e) {
			System.err.println("StockList fail to open");
			e.printStackTrace();
			System.exit(0);
		}
		Scene scene = new Scene(root);
		this.primaryStage.setTitle("StockEy");
		this.primaryStage.initStyle(StageStyle.UNDECORATED);
		this.primaryStage.setScene(scene);
		DragedScene.enableDrag(scene,primaryStage);
		this.primaryStage.show();

	}

	public static void main(String[] args) {
		launch(args);
	}

}
