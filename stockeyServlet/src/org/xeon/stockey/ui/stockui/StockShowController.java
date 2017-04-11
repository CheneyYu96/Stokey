package org.xeon.stockey.ui.stockui;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import org.xeon.stockey.businessLogic.stockAccess.FileBenchmarkSourceImpl;
import org.xeon.stockey.businessLogic.stockAccess.FileStockSourceImpl;
import org.xeon.stockey.businessLogic.stockAccess.StockDailyDataFilterImpl;
import org.xeon.stockey.businessLogic.stockAnalysis.StockAnalysisImpl;
import org.xeon.stockey.businessLogic.utility.NetworkConnectionException;
import org.xeon.stockey.businessLogicService.stockAccessService.StockDailyDataFilterService;
import org.xeon.stockey.businessLogicService.stockAnalysisService.StockAnalysisService;
import org.xeon.stockey.ui.main.MainApp;
import org.xeon.stockey.ui.stockui.FilterDialogController.FilterCallBack;
import org.xeon.stockey.ui.stockui.graph.FunctionPanel;
import org.xeon.stockey.ui.stockui.graph.GeneralGraph;
import org.xeon.stockey.ui.stockui.graph.KGraphPanel;
import org.xeon.stockey.ui.utility.DateUtil;
import org.xeon.stockey.ui.utility.NetExceptionTips;
import org.xeon.stockey.ui.utility.OtherUtil;
import org.xeon.stockey.ui.utility.StockType;
import org.xeon.stockey.vo.CandlestickVO;
import org.xeon.stockey.vo.DailyDataVO;
import org.xeon.stockey.vo.KdjVO;
import org.xeon.stockey.vo.MacdVO;
import org.xeon.stockey.vo.PointVO;
import org.xeon.stockey.vo.RsiVO;
import org.xeon.stockey.vo.StockInfoVO;
import org.xeon.stockey.vo.VolumeVO;

/**
 * @author ymc,Alan
 * @version Last Modified: 04-13
 *
 */
public class StockShowController extends GeneralInterface implements
		FilterCallBack {

	private StockDailyDataFilterService dailyDataFilter;

	private StockAnalysisService stockAnalysis;
	private StockInfoVO stockVO;

	private ObservableList<DailyDataVO> dailyData = FXCollections
			.observableArrayList();

	private ObservableList<DailyDataVO> filterDailyData = FXCollections
			.observableArrayList();
	@FXML
	private TableView<DailyDataVO> dailyTable;
	// 日期
	@FXML
	private TableColumn<DailyDataVO, String> date;
	// 开盘价
	@FXML
	private TableColumn<DailyDataVO, String> open;
	// 收盘价
	@FXML
	private TableColumn<DailyDataVO, String> close;
	// 交易量
	@FXML
	private TableColumn<DailyDataVO, String> volumn;
	// 交易金额
	@FXML
	private TableColumn<DailyDataVO, String> turnover;
	// 最低价
	@FXML
	private TableColumn<DailyDataVO, String> min;
	// 最高价
	@FXML
	private TableColumn<DailyDataVO, String> max;

	private LocalDate beginDate;

	private LocalDate endDate;

	@FXML
	private Label idLabel;
	private StockType stockType;

	private AnchorPane centerPane;
	private AnchorPane formPane;
	private AnchorPane blankPane;
	private KGraphPanel kGraphPanel;
	private FunctionPanel functionPanel;
	private ArrayList<MacdVO> macdVOList = null;
	private ArrayList<KdjVO> kdjVOList = null;
	private ArrayList<RsiVO> rsiVOList = null;
	private ArrayList<CandlestickVO> candlestickVOList = null;
	private ArrayList<VolumeVO> volumeVOList = null;
	private ArrayList<PointVO> turnoverVOList = null;
	private double timeSelectorHeight = 20;
	private LocalDate now = LocalDate.now();
	private ToggleGroup toggleGroup = new ToggleGroup();
	private RadioButton macd;

	private DatePicker startDatePicker = new DatePicker(now.minusMonths(1)),
			endDatePicker = new DatePicker(now);

	@FXML
	private void initialize() {
		date.setCellValueFactory(cellData -> DateUtil.formatProperty(cellData
				.getValue().getDate()));
		open.setCellValueFactory(cellData -> new SimpleStringProperty(cellData
				.getValue().getOpen() + ""));
		close.setCellValueFactory(cellData -> new SimpleStringProperty(cellData
				.getValue().getClose() + ""));
		volumn.setCellValueFactory(cellData -> new SimpleStringProperty(
				OtherUtil.double2String(cellData.getValue().getVolumn())));
		turnover.setCellValueFactory(cellData -> new SimpleStringProperty(
				OtherUtil.double2String(cellData.getValue().getTurnover())));
		max.setCellValueFactory(cell -> new SimpleStringProperty(cell
				.getValue().getHigh() + ""));
		min.setCellValueFactory(cell -> new SimpleStringProperty(cell
				.getValue().getLow() + ""));

	}

	public static Parent launch(MainApp mainApp, StockInfoVO vo,
			StockType stockType) throws IOException {
		FXMLLoader loader = new FXMLLoader(
				StockShowController.class.getResource("StockeySingleShow.fxml"));
		AnchorPane pane = loader.load();

		StockShowController controller = loader.getController();
		controller.setMainApp(mainApp);
		controller.stockVO = vo;
		controller.setValue(stockType);
		controller.centerPane = pane;
		controller.formPane = (AnchorPane) pane.getChildren().get(2);
		controller.showChart();
		return pane;

	}

	public void showFilterDialog() {
		try {
			FilterDialogController.launch(mainApp, stockVO, stockType, this);
		} catch (IOException e) {
			System.err.println("Filter fails to open");
			e.printStackTrace();
		}
	}

	public void showBeginInfo() {
		dailyTable.setItems(dailyData);
	}

	@Override
	public void setDailyData(Collection<DailyDataVO> result) {
		filterDailyData.clear();
		filterDailyData.addAll(result);
		dailyTable.setItems(filterDailyData);
	}

	private boolean setValue(StockType stockType) {
		idLabel.setText(stockVO.getName() + "(" + stockVO.getStockCode()
				+ ")历史交易");
		if (stockType == StockType.BENCHMARKET) {
			dailyDataFilter = new StockDailyDataFilterImpl(
					new FileBenchmarkSourceImpl());
		} else {
			dailyDataFilter = new StockDailyDataFilterImpl(
					new FileStockSourceImpl());
		}
		Collection<DailyDataVO> dailyDataVOs = null;
		try {
			dailyDataVOs = dailyDataFilter.setStockInfoVO(stockVO)
					.filterDate(LocalDate.of(2016, 1, 1), LocalDate.now())
					.getResult();
		} catch (NetworkConnectionException e) {
			dailyDataVOs = new ArrayList<>();
		}
		for (DailyDataVO vo : dailyDataVOs) {
			dailyData.add(vo);
		}
		dailyTable.setItems(dailyData);

		this.stockType = stockType;
		return true;
	}

	/**
	 * 展示K线图以及其他图表
	 */
	public void showChart() {

//		Label title = new Label(stockVO.getName() + "(" + stockVO.getStockCode()
//				+ ")图标信息");


		if (centerPane.getChildren().get(2) instanceof AnchorPane) {
			AnchorPane tmp = (AnchorPane) centerPane.getChildren().get(2);
			if (tmp.getChildren().get(0) instanceof GeneralGraph) {
				return;
			}
		}
		if (blankPane != null) {
			centerPane.getChildren().set(2, blankPane);
			return;
		}
		stockAnalysis = new StockAnalysisImpl();

		getBlankPanel();

		showTimeSelector();
		showTypeChooser();
		showKGraph();
		showOtherGraph();
	}

	/**
	 * 展示时间选择器
	 */
	private void showTimeSelector() {

		startDatePicker.setLayoutX(blankPane.getScaleX() + 40);
		startDatePicker.setLayoutY(blankPane.getScaleY());
		endDatePicker.setLayoutX(blankPane.getScaleX() + 260);
		endDatePicker.setLayoutY(blankPane.getScaleY());
		blankPane.getChildren().addAll(startDatePicker, endDatePicker);

	}

	/**
	 * choose the showing type from daily , weekly or monthly
	 */
	private void showTypeChooser() {

		Button dailyBtn = new Button("日查询");

		Button weeklyBtn = new Button("周查询");

		Button monthlyBtn = new Button("月查询");

		dailyBtn.setLayoutX(blankPane.getScaleX() + 480);
		dailyBtn.setLayoutY(blankPane.getScaleY());
		blankPane.getChildren().add(dailyBtn);
		dailyBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {

				beginDate = startDatePicker.getValue();
				endDate = endDatePicker.getValue();
				getMACDData(beginDate, endDate, stockAnalysis::getMacdD);
				getKDJData(beginDate, endDate, stockAnalysis::getKdjD);
				getRSIData(beginDate, endDate, stockAnalysis::getRsiD);
				getVolumeData(beginDate, endDate, stockAnalysis::getVolumeD);
				getTurnoverData(beginDate, endDate, stockAnalysis::getTurnoverD);
				functionPanel.setMACDData(macdVOList);
				functionPanel.setKDJData(kdjVOList);
				functionPanel.setRSIData(rsiVOList);
				functionPanel.setVolumeData(volumeVOList);
				functionPanel.setTurnoverData(turnoverVOList);
				functionPanel.setTurnoverVisibility(false);
				functionPanel.setVolumeVisibility(false);
				functionPanel.setKDJVisibility(false);
				functionPanel.setRSIVisibility(false);

				showKGraph(beginDate, endDate,
						stockAnalysis::getCandlestickLineD);
				blankPane.getChildren().remove(macd);
				blankPane.getChildren().add(macd);
				macd.setSelected(true);

			}
		});

		weeklyBtn.setLayoutX(blankPane.getScaleX() + 540);
		weeklyBtn.setLayoutY(blankPane.getScaleY());
		blankPane.getChildren().add(weeklyBtn);
		weeklyBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				beginDate = startDatePicker.getValue();
				endDate = endDatePicker.getValue();
				getMACDData(beginDate, endDate, stockAnalysis::getMacdW);
				getKDJData(beginDate, endDate, stockAnalysis::getKdjW);
				getRSIData(beginDate, endDate, stockAnalysis::getRsiW);
				getVolumeData(beginDate, endDate, stockAnalysis::getVolumeW);
				getTurnoverData(beginDate, endDate, stockAnalysis::getTurnoverW);
				functionPanel.setMACDData(macdVOList);
				functionPanel.setKDJData(kdjVOList);
				functionPanel.setRSIData(rsiVOList);
				functionPanel.setVolumeData(volumeVOList);
				functionPanel.setTurnoverData(turnoverVOList);
				functionPanel.setTurnoverVisibility(false);
				functionPanel.setVolumeVisibility(false);
				functionPanel.setKDJVisibility(false);
				functionPanel.setRSIVisibility(false);

				showKGraph(beginDate, endDate,
						stockAnalysis::getCandlestickLineW);
				blankPane.getChildren().remove(macd);
				blankPane.getChildren().add(macd);
				macd.setSelected(true);

			}
		});

		monthlyBtn.setLayoutX(blankPane.getScaleX() + 600);
		monthlyBtn.setLayoutY(blankPane.getScaleY());
		blankPane.getChildren().add(monthlyBtn);
		monthlyBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				beginDate = startDatePicker.getValue();
				endDate = endDatePicker.getValue();
				getMACDData(beginDate, endDate, stockAnalysis::getMacdM);
				getKDJData(beginDate, endDate, stockAnalysis::getKdjM);
				getRSIData(beginDate, endDate, stockAnalysis::getRsiM);
				getVolumeData(beginDate, endDate, stockAnalysis::getVolumeM);
				getTurnoverData(beginDate, endDate, stockAnalysis::getTurnoverM);
				functionPanel.setMACDData(macdVOList);
				functionPanel.setKDJData(kdjVOList);
				functionPanel.setRSIData(rsiVOList);
				functionPanel.setVolumeData(volumeVOList);
				functionPanel.setTurnoverData(turnoverVOList);
				functionPanel.setTurnoverVisibility(false);
				functionPanel.setVolumeVisibility(false);
				functionPanel.setKDJVisibility(false);
				functionPanel.setRSIVisibility(false);

				showKGraph(beginDate, endDate,
						stockAnalysis::getCandlestickLineM);
				blankPane.getChildren().remove(macd);
				blankPane.getChildren().add(macd);
				macd.setSelected(true);
			}
		});

	}

	interface KGraphSource {

		Collection<CandlestickVO> getData(String stockCode,
				LocalDate startDate, LocalDate endDate)
				throws NetworkConnectionException;

	}

	/**
	 * 展示K线图
	 */
	private void showKGraph() {
		showKGraph(now.minusMonths(1), now, stockAnalysis::getCandlestickLineD);
	}

	private void showKGraph(LocalDate startDate, LocalDate endDate,
			KGraphSource source) {
		getKGraphData(startDate, endDate, source);

		if (kGraphPanel != null) {
			blankPane.getChildren().remove(kGraphPanel);
		}
		kGraphPanel = new KGraphPanel(candlestickVOList);
		kGraphPanel.setLayoutX(blankPane.getScaleX());
		kGraphPanel.setLayoutY(blankPane.getScaleY() + timeSelectorHeight);
		kGraphPanel.setPrefSize(blankPane.getPrefWidth(),
				blankPane.getPrefHeight() * 2 / 3 - timeSelectorHeight);

		blankPane.getChildren().add(kGraphPanel);
	}

	/**
	 * 展示其他图表
	 */
	private void showOtherGraph() {
		// 默认显示MACD线
		getMACDData();
		showMACDChart();
		showKDJChart();
		showRSIChart();
		showVolumeChart();
		showTurnoverChart();
	}

	private void showMACDChart() {
		functionPanel = new FunctionPanel();
		functionPanel.setMACDData(macdVOList);

		functionPanel.setLayoutX(blankPane.getScaleX());
		functionPanel.setLayoutY(blankPane.getScaleY()
				+ kGraphPanel.getPrefHeight());
		functionPanel.setPrefSize(blankPane.getPrefWidth() * 8 / 9,
				blankPane.getPrefHeight() * 1 / 3);

		// 添加到K线图下面的位置
		blankPane.getChildren().add(functionPanel);

		macd = new RadioButton("MACD");
		macd.setToggleGroup(toggleGroup);
		macd.setSelected(true);
		macd.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (macd.isSelected()) {
					functionPanel.setKDJVisibility(false);
					functionPanel.setRSIVisibility(false);
					functionPanel.setVolumeVisibility(false);
					functionPanel.setTurnoverVisibility(false);
					functionPanel.setMACDVisibility(true);
				}
			}
		});
		macd.setLayoutX(blankPane.getScaleX() + functionPanel.getPrefWidth());
		macd.setLayoutY(blankPane.getScaleY() + kGraphPanel.getPrefHeight());
		blankPane.getChildren().add(macd);
	}

	private void showKDJChart() {
		RadioButton kdj = new RadioButton("KDJ");
		kdj.setToggleGroup(toggleGroup);
		kdj.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (kdj.isSelected()) {
					functionPanel.setMACDVisibility(false);
					functionPanel.setRSIVisibility(false);
					functionPanel.setVolumeVisibility(false);
					functionPanel.setTurnoverVisibility(false);
					if (!functionPanel.isKDJRunned()) {
						getKDJData();
						functionPanel.setKDJData(kdjVOList);
					}
					functionPanel.setKDJVisibility(true);
				}
			}
		});
		kdj.setLayoutX(blankPane.getScaleX() + functionPanel.getPrefWidth());
		kdj.setLayoutY(blankPane.getScaleY() + 30 + kGraphPanel.getPrefHeight());
		blankPane.getChildren().add(kdj);
	}

	private void showRSIChart() {
		RadioButton rsi = new RadioButton("RSI");
		rsi.setToggleGroup(toggleGroup);
		rsi.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				if (rsi.isSelected()) {
					functionPanel.setMACDVisibility(false);
					functionPanel.setKDJVisibility(false);
					functionPanel.setVolumeVisibility(false);
					functionPanel.setTurnoverVisibility(false);
					if (!functionPanel.isRSIRunned()) {
						getRSIData();
						functionPanel.setRSIData(rsiVOList);
					}
					functionPanel.setRSIVisibility(true);
				}
			}
		});
		rsi.setLayoutX(blankPane.getScaleX() + functionPanel.getPrefWidth());
		rsi.setLayoutY(blankPane.getScaleY() + 60 + kGraphPanel.getPrefHeight());
		blankPane.getChildren().add(rsi);
	}

	private void showVolumeChart() {
		RadioButton volume = new RadioButton("成交量");
		volume.setToggleGroup(toggleGroup);
		volume.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				functionPanel.setMACDVisibility(false);
				functionPanel.setKDJVisibility(false);
				functionPanel.setRSIVisibility(false);
				functionPanel.setTurnoverVisibility(false);
				if (!functionPanel.isVolumeRunned()) {
					getVolumeData();
					functionPanel.setVolumeData(volumeVOList);
				}
				functionPanel.setVolumeVisibility(true);
			}
		});
		volume.setLayoutX(blankPane.getScaleX() + functionPanel.getPrefWidth());
		volume.setLayoutY(blankPane.getScaleY() + 90
				+ kGraphPanel.getPrefHeight());
		blankPane.getChildren().add(volume);
	}

	private void showTurnoverChart() {
		RadioButton turnover = new RadioButton("换手率");
		turnover.setToggleGroup(toggleGroup);
		turnover.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				functionPanel.setMACDVisibility(false);
				functionPanel.setKDJVisibility(false);
				functionPanel.setRSIVisibility(false);
				functionPanel.setVolumeVisibility(false);
				if (!functionPanel.isTurnoverRunned()) {
					getTurnoverData();
					functionPanel.setTurnoverData(turnoverVOList);
				}
				functionPanel.setTurnoverVisibility(true);
			}
		});
		turnover.setLayoutX(blankPane.getScaleX()
				+ functionPanel.getPrefWidth());
		turnover.setLayoutY(blankPane.getScaleY() + 120
				+ kGraphPanel.getPrefHeight());
		blankPane.getChildren().add(turnover);
	}

	private void getKGraphData(LocalDate startDate, LocalDate endDate,
			KGraphSource source) {
		Collection<CandlestickVO> candlestickVOs = null;
		try {

			candlestickVOs = source.getData(stockVO.getStockCode(), startDate,
					endDate);

		} catch (NetworkConnectionException e) {

			new NetExceptionTips();

		}

		if (candlestickVOs != null) {
			candlestickVOList = new ArrayList<>();
			for (CandlestickVO vo : candlestickVOs) {
				candlestickVOList.add(vo);
			}
		}
	}

	private void getKGraphData() {
		getKGraphData(now.minusMonths(1), now,
				stockAnalysis::getCandlestickLineD);
	}

	interface MACDSource {
		Collection<MacdVO> getData(String stockCode, LocalDate startDate,
				LocalDate endDate) throws NetworkConnectionException;
	}

	/**
	 * 获得MACD数据
	 */
	private void getMACDData(LocalDate startDate, LocalDate endDate,
			MACDSource source) {
		Collection<MacdVO> macdVOs = null;
		try {
			macdVOs = source
					.getData(stockVO.getStockCode(), startDate, endDate);
		} catch (NetworkConnectionException e) {
			new NetExceptionTips();
			e.printStackTrace();
		}
		if (macdVOs != null) {
			macdVOList = new ArrayList<MacdVO>();
			for (MacdVO vo : macdVOs) {
				macdVOList.add(vo);
			}
		}
	}

	/**
	 * 获得默认一个月的数据
	 */
	private void getMACDData() {
		getMACDData(now.minusMonths(1), now, stockAnalysis::getMacdD);
	}

	interface KDJSource {

		Collection<KdjVO> getData(String stockCode, LocalDate startDate,
				LocalDate endDate) throws NetworkConnectionException;

	}

	/**
	 * 获得KDJ数据
	 */
	private void getKDJData(LocalDate startDate, LocalDate endDate,
			KDJSource source) {
		Collection<KdjVO> kdjVOs = null;
		try {
			kdjVOs = source.getData(stockVO.getStockCode(), startDate, endDate);
		} catch (NetworkConnectionException e) {
			new NetExceptionTips();
			e.printStackTrace();
		}
		if (kdjVOs != null) {
			kdjVOList = new ArrayList<KdjVO>();
			for (KdjVO vo : kdjVOs) {
				kdjVOList.add(vo);
			}
		}
	}

	private void getKDJData() {
		getKDJData(now.minusMonths(1), now, stockAnalysis::getKdjD);
	}

	interface RSISource {

		Collection<RsiVO> getData(String stockCode, LocalDate startDate,
				LocalDate endDate) throws NetworkConnectionException;

	}

	/**
	 * 获得RSI数据
	 */
	private void getRSIData(LocalDate startDate, LocalDate endDate,
			RSISource source) {
		Collection<RsiVO> rsiVOs = null;
		try {
			rsiVOs = source.getData(stockVO.getStockCode(), startDate, endDate);
		} catch (NetworkConnectionException e) {
			new NetExceptionTips();
			e.printStackTrace();
		}
		if (rsiVOs != null) {
			rsiVOList = new ArrayList<RsiVO>();
			for (RsiVO vo : rsiVOs) {
				rsiVOList.add(vo);
			}
		}
	}

	private void getRSIData() {
		getRSIData(now.minusMonths(1), now, stockAnalysis::getRsiD);
	}

	interface VolumeSource {

		Collection<VolumeVO> getData(String stockCode, LocalDate startDate,
				LocalDate endDate) throws NetworkConnectionException;

	}

	private void getVolumeData(LocalDate startDate, LocalDate endDate,
			VolumeSource source) {
		Collection<VolumeVO> volumeVO = null;
		try {
			volumeVO = source.getData(stockVO.getStockCode(), startDate,
					endDate);
		} catch (NetworkConnectionException e) {
			new NetExceptionTips();
			e.printStackTrace();
		}
		if (volumeVO != null) {
			volumeVOList = new ArrayList<VolumeVO>();
			for (VolumeVO vo : volumeVO) {
				volumeVOList.add(vo);
			}
		}
	}

	private void getVolumeData() {
		getVolumeData(now.minusMonths(1), now, stockAnalysis::getVolumeD);
	}

	interface TurnoverSource {

		Collection<PointVO> getData(String stockCode, LocalDate startDate,
				LocalDate endDate) throws NetworkConnectionException;

	}

	private void getTurnoverData(LocalDate startDate, LocalDate endDate,
			TurnoverSource source) {
		Collection<PointVO> turnoverVO = null;
		try {
			turnoverVO = source.getData(stockVO.getStockCode(), startDate,
					endDate);
		} catch (NetworkConnectionException e) {
			new NetExceptionTips();
			e.printStackTrace();
		}
		if (turnoverVO != null) {
			turnoverVOList = new ArrayList<PointVO>();
			for (PointVO vo : turnoverVO) {
				turnoverVOList.add(vo);
			}
		}
	}

	private void getTurnoverData() {
		getTurnoverData(now.minusMonths(1), now, stockAnalysis::getTurnoverD);
	}

	public void showForm() {

		if (centerPane.getChildren().get(2) == formPane) {
			return;
		}

		centerPane.getChildren().set(2, formPane);
	}

	public Pane getBlankPanel() {

		if (blankPane == null) {

			blankPane = new AnchorPane();

			AnchorPane tmp = (AnchorPane) centerPane.getChildren().get(2);

			double x = tmp.getLayoutX();

			double y = tmp.getLayoutY();

			double width = tmp.getPrefWidth();

			double height = tmp.getPrefHeight();

			blankPane.setLayoutX(x);

			blankPane.setLayoutY(y);

			blankPane.setPrefHeight(height);

			blankPane.setPrefWidth(width);

			centerPane.getChildren().set(2, blankPane);
		}
		return blankPane;
	}
}
