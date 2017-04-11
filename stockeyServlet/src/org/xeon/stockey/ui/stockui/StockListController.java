package org.xeon.stockey.ui.stockui;

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

import org.xeon.stockey.businessLogic.stockAccess.FileStockSourceImpl;
import org.xeon.stockey.businessLogic.stockAccess.StockInfoFilterImpl;
import org.xeon.stockey.businessLogic.utility.NetworkConnectionException;
import org.xeon.stockey.ui.main.MainApp;
import org.xeon.stockey.ui.utility.NetExceptionTips;
import org.xeon.stockey.ui.utility.SimpleDoubleProperty;
import org.xeon.stockey.ui.utility.StockType;
import org.xeon.stockey.vo.StockInfoVO;

/**
 * @author ymc,Alan
 * @version Last modified：2016-03-11 00:21
 *
 */
public class StockListController extends GeneralInterface {

	private StockInfoFilterImpl stockInfoFilter;

	private ObservableList<StockInfoVO> stockData = FXCollections.observableArrayList();
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


//	public TableColumn<StockInfoVO, String> location;
//	
//	public TableColumn<StockInfoVO, String> industy;

	private BorderPane panel;

	private AnchorPane centerPane;

	private AnchorPane changedPane;

	public StockListController() {
		stockInfoFilter = new StockInfoFilterImpl();
	}

	public static Parent launch(MainApp mainApp) throws IOException {
		FXMLLoader loader = new FXMLLoader(
				StockListController.class.getResource("StockList.fxml"));
		Pane pane = loader.load();
		StockListController controller = loader.getController();
		controller.setMainApp(mainApp);
		controller.setStockData();

		if(pane instanceof BorderPane) {
			controller.panel =(BorderPane) pane;

			controller.centerPane = (AnchorPane) controller.panel.getCenter();

			controller.changedPane = (AnchorPane) controller.centerPane.getChildren().get(2);

		}
	
		return pane;
	}

	@FXML
	private void initialize() {
		stockCode.setCellValueFactory(cellData -> new SimpleStringProperty(
				cellData.getValue().getStockCode()));
		name.setCellValueFactory(cellData -> new SimpleStringProperty(cellData
				.getValue().getName()));
		open.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getLatestDailyDataVO().getOpen()));
		close.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getLatestDailyDataVO().getClose()));
//		industy.setCellValueFactory(cell->new SimpleStringProperty(cell.getValue().getType()));
//		location.setCellValueFactory(cell->new SimpleStringProperty(cell.getValue().getRegion()));
	}
	
	private void setStockData() {
		Collection<StockInfoVO> stockInfoVOs;
		try {
			stockInfoVOs = stockInfoFilter.setStockSource(new FileStockSourceImpl()).getResult();
		}
		catch (NetworkConnectionException e){
			stockInfoVOs = new ArrayList<>();
			new NetExceptionTips();
		}
		Iterator<StockInfoVO> iterator = stockInfoVOs.iterator();
		for (; iterator.hasNext();) {
			stockData.add(iterator.next());
		}
		stockTable.setItems(stockData);
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
							StockType.NORMALMARKET);
				} catch (IOException e) {
					System.err.println("StockShow fails to open");
					e.printStackTrace();
				}

				panel.setCenter(pane);
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

	public void toList(){

		if(centerPane.getChildren().get(2)==changedPane){

			return;

		}

		centerPane.getChildren().set(2, changedPane);

	}

	public void toStatistic(){

		if(centerPane.getChildren().get(2)!=changedPane){

			return;

		}

		try {

			Pane tmp = ContrastController.launch(centerPane);

			tmp.setLayoutX(changedPane.getScaleX());

			tmp.setLayoutY(changedPane.getLayoutY());

			tmp.setPrefWidth(changedPane.getPrefWidth());

			tmp.setPrefHeight(changedPane.getPrefHeight());

		}
		catch (IOException e){

			System.err.println("read file wrong");

		}

	}
	
}
