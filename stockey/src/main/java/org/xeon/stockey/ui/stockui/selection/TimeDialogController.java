package org.xeon.stockey.ui.stockui.selection;

import java.io.IOException;
import java.time.LocalDate;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import org.xeon.stockey.ui.utility.DragedScene;

public class TimeDialogController {

	@FXML
	DatePicker startDate, endDate;
	@FXML
	Button cancel, confirm;

	private Stage stage;
	private TimeCallBack parent;

	public static void launch(TimeCallBack parent) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(StockShowController.class
				.getResource("TimeDialog.fxml"));
		AnchorPane timeDialog = loader.load();
		Stage dialogStage = new Stage();
		dialogStage.initModality(Modality.WINDOW_MODAL);
		dialogStage.initStyle(StageStyle.UNDECORATED);

		Scene scene = new Scene(timeDialog);
		dialogStage.setScene(scene);
		DragedScene.enableDrag(scene, dialogStage);
		dialogStage.show();

		TimeDialogController controller = loader.getController();
		controller.init(parent, dialogStage);
	}

	/**
	 * 初始化
	 * 
	 * @param parent
	 * @param stage
	 */
	public void init(TimeCallBack parent, Stage stage) {
		this.parent = parent;
		this.stage = stage;
	}

	/**
	 * 点击确认，获得开始和结束时间，刷新数据
	 */
	public void refreshData() {
		parent.refreshData(startDate.getValue(), endDate.getValue());
		cancelTimeDialog();
	}

	/**
	 * 取消时间刷新
	 */
	public void cancelTimeDialog() {
		stage.close();
	}

	/**
	 * 时间选择框的回调接口
	 * 
	 * @author Alan
	 *
	 */
	public interface TimeCallBack {
		/**
		 * 刷新数据
		 * 
		 * @param startTime
		 * @param endTime
		 */
		void refreshData(LocalDate startTime, LocalDate endTime);
	}
}
