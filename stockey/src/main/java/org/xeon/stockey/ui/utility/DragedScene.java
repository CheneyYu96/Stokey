package org.xeon.stockey.ui.utility;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/** 
 * @author ymc 
 * @version 创建时间：2016年3月12日 下午2:04:57 
 *
 */
public class DragedScene {
	
	static double ix;
	static double iy;

	public static void enableDrag(Scene scene,Stage stage) {

		scene.setOnMousePressed(event -> {
			ix = event.getScreenX() - stage.getX();
			iy = event.getScreenY() - stage.getY();
		});
		scene.setOnMouseDragged(event -> {
			stage.setX(event.getScreenX() - ix);
			stage.setY(event.getScreenY() - iy);
		});
	}

}
