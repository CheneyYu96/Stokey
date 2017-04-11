package org.xeon.stockey.ui.utility;

import javafx.scene.control.Alert;

/**
 * 异常弹窗
 * 
 * @author Alan
 *
 */
public class ExceptionTips {
	Alert alert;

	public ExceptionTips(String content) {
		initAlert("出现问题", "请注意：",content);
	}
	
	public ExceptionTips(String title,String content){
		initAlert(title, "请注意：",content);
	}
	
	public ExceptionTips(String title,String headerText,String content){
		initAlert(title, headerText, content);
	}

	private void initAlert(String title, String headerText, String content) {
		alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(headerText);
		alert.setContentText(content);
		alert.showAndWait();
	}
}
