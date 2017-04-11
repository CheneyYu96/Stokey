package org.xeon.stockey.ui.main;

import java.io.IOException;

import org.xeon.stockey.ui.stockui.StockListController;
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

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		Parent root = null;
		try {
			root = StockListController.launch(this);
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

//	/**
//	 * 拖曳界面监听
//	 */
//	double ix;
//	double iy;
//
//	public void enableDrag(Scene scene) {
//
//		scene.setOnMousePressed(event -> {
//			ix = event.getScreenX() - primaryStage.getX();
//			iy = event.getScreenY() - primaryStage.getY();
//		});
//		scene.setOnMouseDragged(event -> {
//			primaryStage.setX(event.getScreenX() - ix);
//			primaryStage.setY(event.getScreenY() - iy);
//		});
//	}

	public static void main(String[] args) {
		launch(args);
	}

	

}
