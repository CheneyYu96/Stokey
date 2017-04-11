package org.xeon.stockey.ui.stockui;

import java.io.IOException;
import java.util.Collection;

import org.xeon.stockey.businessLogic.stockAccess.DBStockSourceImpl;
import org.xeon.stockey.businessLogic.stockAccess.FileBenchmarkSourceImpl;
import org.xeon.stockey.businessLogic.stockAccess.FileStockSourceImpl;
import org.xeon.stockey.businessLogic.stockAccess.StockDailyDataFilterImpl;
import org.xeon.stockey.businessLogic.utility.NetworkConnectionException;
import org.xeon.stockey.businessLogicService.stockAccessService.StockDailyDataFilterService;
import org.xeon.stockey.ui.main.MainApp;
import org.xeon.stockey.ui.stockui.market.StockMarketController;
import org.xeon.stockey.ui.utility.DragedScene;
import org.xeon.stockey.ui.utility.FilterType;
import org.xeon.stockey.ui.utility.NetExceptionTips;
import org.xeon.stockey.ui.utility.StockType;
import org.xeon.stockey.vo.DailyDataVO;
import org.xeon.stockey.vo.StockInfoVO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class FilterDialogController {
	@FXML
	private Button cancel;
	@FXML
	private Button confirm;
	@FXML
	private Button add;
	@FXML
	private Button delete;
	@FXML
	private TextField startCondition;
	@FXML
	private TextField endCondition;
	@FXML
	private DatePicker startDate;
	@FXML
	private DatePicker endDate;
	@FXML
	private ComboBox<String> filterType;
	@FXML
	private ListView<String> listview;

	ObservableList<String> listViewList = FXCollections.observableArrayList();

	StockDailyDataFilterService sddfs;

	private Stage stage;

	private FilterCallBack parent;

	public static void launch(MainApp mainApp, StockInfoVO stockInfoVO,
			StockType stockType, FilterCallBack parent) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(StockMarketController.class
				.getResource("FilterDialog.fxml"));
		AnchorPane filterPane = loader.load();
		Stage dialogStage = new Stage();
		dialogStage.setTitle("过滤器");
		dialogStage.initModality(Modality.WINDOW_MODAL);
		dialogStage.initStyle(StageStyle.UNDECORATED);
		dialogStage.initOwner(mainApp.getPrimaryStage());

		Scene scene = new Scene(filterPane);
		dialogStage.setScene(scene);
		DragedScene.enableDrag(scene, dialogStage);
		dialogStage.show();

		FilterDialogController controller = loader.getController();
		controller.setStage(dialogStage);
		controller.setTypes();
		controller.init(parent, stockInfoVO, stockType);
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public void init(FilterCallBack parent, StockInfoVO stockInfo,
			StockType stockType) {
		sddfs = new StockDailyDataFilterImpl();
		if (stockType == StockType.BENCHMARKET) {
			sddfs.setStockSource(new FileBenchmarkSourceImpl());
		} else {
			sddfs.setStockSource(new DBStockSourceImpl());
		}
		sddfs.setStockInfoVO(stockInfo);
		this.parent = parent;

	}

	/**
	 * 向右方listview中添加内容
	 */
	public void add() {
		String value = "";
		if (startDate.isVisible()) {
			value = filterType.getValue() + " "
					+ startDate.getValue().toString() + "~"
					+ endDate.getValue().toString();
			sddfs.filterDate(startDate.getValue(), endDate.getValue());
		} else {
			String start = startCondition.getText(), end = endCondition
					.getText();
			value = filterType.getValue() + " " + start + "~" + end;
			double startNum = Double.parseDouble(start);
			double endNum = Double.parseDouble(end);
			switch (filterType.getValue()) {
			case "最高价":
				sddfs.filterHigh(startNum, endNum);
				break;
			case "最低价":
				sddfs.filterLow(startNum, endNum);
				break;
			case "开盘价":
				sddfs.filterOpen(startNum, endNum);
				break;
			case "收盘价":
				sddfs.filterClose(startNum, endNum);
				break;
			case "交易量":
				sddfs.filterVolume(startNum, endNum);
				break;
			case "交易金额":
				sddfs.filterTurnover(startNum, endNum);
				break;
			default:
				break;
			}
		}
		listViewList.add(value);
		listview.setItems(listViewList);

	}

	/**
	 * 删除listView中选中内容
	 */
	public void delete() {
		listViewList.remove(listview.getSelectionModel().getSelectedIndex());
		listview.setItems(listViewList);
	}

	public void setTypes() {
		filterType.setItems(FilterType.DAILYDATA.getTypes());
		filterType.setValue("日期");
	}

	/**
	 * 根据选择的filterType改变选项内容
	 */
	public void modifyChoice() {
		if (filterType.getValue().equals("日期")) {
			startDate.setVisible(true);
			endDate.setVisible(true);
		} else {
			startDate.setVisible(false);
			endDate.setVisible(false);
		}
	}

	/**
	 * 取消过滤器
	 */
	public void cancelFilter() {
		stage.close();
	}

	/**
	 * 开始过滤
	 */
	public void getFilter() {
		try {
			parent.setDailyData(sddfs.getResult());
		}
		catch (NetworkConnectionException e){
			new NetExceptionTips();

		}

		cancelFilter();
	}

	public interface FilterCallBack {
		public void setDailyData(Collection<DailyDataVO> result);
	}

}
