package org.xeon.stockey.ui.stockui.market;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import org.xeon.stockey.businessLogic.stockAccess.DBStockSourceImpl;
import org.xeon.stockey.businessLogic.stockAccess.FileBenchmarkSourceImpl;
import org.xeon.stockey.businessLogic.stockAccess.StockInfoFilterImpl;
import org.xeon.stockey.businessLogic.utility.NetworkConnectionException;
import org.xeon.stockey.businessLogicService.stockAccessService.StockSourceService;
import org.xeon.stockey.ui.main.MainApp;
import org.xeon.stockey.ui.stockui.GeneralInterface;
import org.xeon.stockey.ui.stockui.selection.StockShowController;
import org.xeon.stockey.ui.utility.NetExceptionTips;
import org.xeon.stockey.ui.utility.SimpleDoubleProperty;
import org.xeon.stockey.ui.utility.StockType;
import org.xeon.stockey.vo.StockInfoVO;

/**
 * @author Alan
 * @version Last modified: 2016-03-10 23:38:22
 *
 */


public class StockMarketController extends GeneralInterface {

	/**
	 * 股票过滤器
	 */
	private StockInfoFilterImpl stockInfoFilter;

	private StockSourceService sourceService;

	/**
	 * 静态股票信息
	 */
	private ObservableList<StockInfoVO> stockData = FXCollections
			.observableArrayList();

	// 股票表格
	@FXML
	private TableView<StockInfoVO> stockTable;
	// 股票代码
	@FXML
	private TableColumn<StockInfoVO, String> stockCode;
	// 股票名称
	@FXML
	private TableColumn<StockInfoVO, String> name;
	// 开盘价
	@FXML
	private TableColumn<StockInfoVO, Double> open;
	// 收盘价
	@FXML
	private TableColumn<StockInfoVO, Double> close;

	private AnchorPane panel;

	public StockMarketController() {
	}

	private void setStockData() {
		StockInfoVO stockInfoVO = null;
		try {
			stockInfoVO = sourceService.getStockInfo("bm000300");
		}
		catch (NetworkConnectionException e){
			new NetExceptionTips();
		}
		stockData.add(stockInfoVO);
	}

	public static Parent launch(MainApp mainApp) throws IOException {
		FXMLLoader loader = new FXMLLoader(
				StockMarketController.class.getResource("MarketSingle.fxml"));
		Pane pane = loader.load();
		StockMarketController controller = loader.getController();
		controller.setMainApp(mainApp);
		controller.setStockData();
		if(pane instanceof AnchorPane) {
			controller.panel = (AnchorPane) pane;
		}
		return pane;
	}

	@Override
	public void setMainApp(MainApp mainApp) {
		super.setMainApp(mainApp);
		stockTable.setItems(stockData);
	}

	@FXML
	private void initialize() {
		sourceService = new DBStockSourceImpl();

		stockInfoFilter = new StockInfoFilterImpl(sourceService);
		stockCode.setCellValueFactory(cellData -> new SimpleStringProperty(
				cellData.getValue().getStockCode()));
		name.setCellValueFactory(cellData -> new SimpleStringProperty(cellData
				.getValue().getName()));
		open.setCellValueFactory(cellData -> {
			double o = 0;
			try {
//				System.err.println("cell is null? " + sourceService.
//						getLatestDailyData(cellData.getValue().getStockCode()));
				o = sourceService.
						getLatestDailyData(cellData.getValue().getStockCode()).
						getOpen();
			} catch (NetworkConnectionException e) {
				e.printStackTrace();
			}
			return new SimpleDoubleProperty(o);

		});
		close.setCellValueFactory(cellData -> {
			double o = 0;
			try {
				o = sourceService.getLatestDailyData(cellData.getValue().getStockCode()).getClose();
			} catch (NetworkConnectionException e) {
				e.printStackTrace();
			}
			return new SimpleDoubleProperty(o);

		});

	}

	@FXML
	private void jumpToStock(MouseEvent event) {
		if (event.getClickCount() == 2) {
			StockInfoVO stockInfoVO = stockTable.getSelectionModel()
					.getSelectedItem();
			if (stockInfoVO != null) {
				Parent pane = null;
				try {
					pane = StockShowController.launch(mainApp, stockInfoVO,
							StockType.BENCHMARKET);
				} catch (IOException e) {
					System.err.println("StockShow fail to open");
					e.printStackTrace();
				}

				BorderPane root = (BorderPane) mainApp.getRoot();

				root.setCenter(pane);

//				Scene scene = new Scene(pane);
//				mainApp.showOther(scene);
			} else {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("No Selection");
				alert.setHeaderText("Tips:");
				alert.setContentText("please select a stock");
				alert.showAndWait();
			}
		}
	}
}
