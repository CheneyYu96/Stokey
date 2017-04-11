package org.xeon.stockey.ui.stockui;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import org.apache.commons.collections.map.HashedMap;
import org.xeon.stockey.businessLogic.stockAccess.FileBenchmarkSourceImpl;
import org.xeon.stockey.businessLogic.stockAccess.FileStockSourceImpl;
import org.xeon.stockey.businessLogic.stockAnalysis.StockAnalysisImpl;
import org.xeon.stockey.businessLogic.utility.NetworkConnectionException;
import org.xeon.stockey.businessLogicService.stockAccessService.StockSourceService;
import org.xeon.stockey.businessLogicService.stockAnalysisService.StockAnalysisService;
import org.xeon.stockey.ui.stockui.graph.PieChartBuilder;
import org.xeon.stockey.ui.utility.ExceptionTips;
import org.xeon.stockey.ui.utility.NetExceptionTips;
import org.xeon.stockey.ui.utility.OtherUtil;
import org.xeon.stockey.ui.utility.SimpleDoubleProperty;
import org.xeon.stockey.vo.ContrastVO;
import org.xeon.stockey.vo.PieVO;
import org.xeon.stockey.vo.PointVO;
import org.xeon.stockey.vo.StockInfoVO;

/**
 * 对比界面的Controller Created by Sissel on 2016/4/13.
 */
public class ContrastController {
	private Set<StockInfoVO> selectedStocks = new HashSet<>();
	private Collection<StockInfoVO> allStocks = new LinkedList<>();
	private StockSourceService stockSource = new FileStockSourceImpl();
	private StockSourceService benchmarkSource = new FileBenchmarkSourceImpl();
	private StockAnalysisService stockAnalysisService = new StockAnalysisImpl();

	public ChoiceBox<String> addChoiceBox;
	public TextField addTextField;

	public TableView<ContrastVO> compareTableView;
	public TableColumn<ContrastVO, String> stockNameColumn;
	public TableColumn<ContrastVO, Double> peColumn;
	public TableColumn<ContrastVO, Double> pbColumn;
	public TableColumn<ContrastVO, Double> changeWeek1Column;
	public TableColumn<ContrastVO, Double> varianceWeek1Column;
	public TableColumn<ContrastVO, Double> changeMonth1Column;
	public TableColumn<ContrastVO, Double> varianceMonth1Column;
	public TableColumn<ContrastVO, String> volumeWeek1Column;

	public ChoiceBox<String> stock1ChoiceBox;
	public ChoiceBox<String> stock2ChoiceBox;
	public Label covarianceLabel;
	public LineChart<String, Number> lineChart;
	public PieChart pieChart;
	private PieVO industryPieVO, selfSelectionPieVO;
	public Button selfSelectionButton;
	public Button industryButton;
	private Map<String, XYChart.Series<String, Number>> seriesMap;
	private Map<String, Double> selfData = new HashMap<String, Double>(),
			industryData = new HashMap<String, Double>();
	private AnchorPane panel;
	/**
	 * 行业饼状图模块是否运行过
	 */
	private boolean isIndustryRunned = false;
	private boolean isIndustryRunning = false;

	private void initStockInfo() {
		try {
			allStocks.addAll(stockSource.getAllStockInfo());
			allStocks.addAll(benchmarkSource.getAllStockInfo());
		} catch (NetworkConnectionException e) {
			new NetExceptionTips();
			e.printStackTrace();
		}
	}

	public static Pane launch(Pane parent) throws IOException {

		FXMLLoader loader = new FXMLLoader(
				StockMarketController.class.getResource("Contrast.fxml"));

		Pane pane = loader.load();

		ContrastController controller = loader.getController();

		if (pane instanceof BorderPane) {
			controller.panel = (AnchorPane) pane;
		}

		parent.getChildren().set(2, pane);

		return pane;

	}

	@FXML
	private void initialize() {

		seriesMap = new HashedMap();
		initStockInfo();

		List<String> stringList = allStocks.stream()
				.map(o -> o.getStockCode() + " " + o.getName())
				.collect(Collectors.toList());

		addChoiceBox.setItems(FXCollections.observableList(stringList));
		addChoiceBox.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> {
					String[] ss = newValue.split(" ");
					addTextField.setText(ss[0]);
				});

		stockNameColumn.setCellValueFactory(cell -> new SimpleStringProperty(
				cell.getValue().getCodeAndName()));
		peColumn.setCellValueFactory(cell -> new SimpleDoubleProperty(cell
				.getValue().pe));
		pbColumn.setCellValueFactory(cell -> new SimpleDoubleProperty(cell
				.getValue().pb));
		changeWeek1Column.setCellValueFactory(cell -> new SimpleDoubleProperty(
				cell.getValue().changeWeek1));
		changeMonth1Column
				.setCellValueFactory(cell -> new SimpleDoubleProperty(cell
						.getValue().changeMonth1));
		varianceWeek1Column
				.setCellValueFactory(cell -> new SimpleDoubleProperty(cell
						.getValue().varianceWeek1));
		varianceMonth1Column
				.setCellValueFactory(cell -> new SimpleDoubleProperty(cell
						.getValue().varianceMonth1));
		volumeWeek1Column.setCellValueFactory(cell -> new SimpleStringProperty(
				OtherUtil.double2String(cell.getValue().volumeWeek1)));
		compareTableView.getSelectionModel().setSelectionMode(
				SelectionMode.MULTIPLE);

		stock1ChoiceBox.setItems(FXCollections.observableList(stringList));
		stock1ChoiceBox.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> {
					calculateCorrelationCoefficient();
				});

		stock2ChoiceBox.setItems(FXCollections.observableList(stringList));
		stock2ChoiceBox.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> {
					calculateCorrelationCoefficient();
				});
	}

	@FXML
	/**
	 * 自选股对比
	 */
	private void selfSelect() {
		pieChart = new PieChartBuilder(pieChart).setData(selfData).build();
	}

	private void removeSelect(String stockCode) {
		PieChartBuilder pieChartBuilder = new PieChartBuilder(pieChart,
				selfData);
		pieChart = pieChartBuilder.removeData(stockCode).build();
		selfData.remove(stockCode);
	}

	@FXML
	private void industrySelect() {
		isIndustryRunning = true;
		// 如果运行过就直接加载数据
		if (!isIndustryRunned) {
			isIndustryRunned = true;
			try {
				industryPieVO = stockAnalysisService.getIndustryInfluencePie(
						LocalDate.now().minusMonths(1), LocalDate.now());
				Map<String, Double> sharedMap = industryPieVO.shareMap;
				for (String key : sharedMap.keySet()) {
					double shareValue = sharedMap.get(key);
					// 如果industryData中没有该数据就添加
					if (industryData.get(key) == null) {
						industryData.put(key, shareValue);
					}
				}
			} catch (NetworkConnectionException e) {
				new NetExceptionTips();
			}
			pieChart = new PieChartBuilder(pieChart).addIndustryData(
					industryPieVO).build();
		}
		pieChart = new PieChartBuilder(pieChart).setData(industryData).build();
	}

	private void calculateCorrelationCoefficient() {
		try {
			String[] ss = stock1ChoiceBox.getValue().split(" ");
			String stockCode1 = ss[0];
			ss = stock2ChoiceBox.getValue().split(" ");
			String stockCode2 = ss[0];

			if (!stockCode1.equals(stockCode2)) {
				LocalDate now = LocalDate.now();
				LocalDate begin = now.minusWeeks(1);
				double correlationCoefficient = stockAnalysisService
						.getCorrelationCoefficientD(stockCode1, stockCode2,
								begin, now);
				covarianceLabel.setText(correlationCoefficient + " ");
			}
		} catch (NullPointerException e) {
			covarianceLabel.setText("暂无数据");
		} catch (NetworkConnectionException e) {
			new NetExceptionTips();
		}
	}

	public void addStock(ActionEvent actionEvent) {
		// judge if in allStocks
		String stockCode = addTextField.getText();

		StockInfoVO stockInfoVO = null;
		boolean legal = false;
		for (StockInfoVO stock : allStocks) {
			if (stockCode.equals(stock.getStockCode())) {
				legal = true;
				stockInfoVO = stock;
				break;
			}
		}

		if (!legal) {
			new ExceptionTips("输入不符合股票代码规则");
			return;
		}

		// judge if already in selectedStocks
		if (selectedStocks.contains(stockInfoVO)) {
			new ExceptionTips("已经添加了相同的股票");
			return;
		}

		try {
			ContrastVO contrastVO = new ContrastVO(stockInfoVO);
			compareTableView.getItems().add(contrastVO);
			selectedStocks.add(stockInfoVO);
			// 向折线图添加数据
			setLineChart(stockInfoVO);
			// 向饼状图添加数据
			addSelfSelectionPie(stockCode);

		} catch (NetworkConnectionException e) {
			new NetExceptionTips();
		}

	}

	/**
	 * 在饼状图添加选择的数据
	 * 
	 * @param stockCode
	 */
	private void addSelfSelectionPie(String stockCode) {
		ArrayList<String> stockCodes = new ArrayList<String>();
		for (StockInfoVO vo : selectedStocks) {
			String addedStockCode = vo.getStockCode();
			// 不添加大盘股
			if (!addedStockCode.startsWith("bm")) {
				stockCodes.add(addedStockCode);
			}
		}

		try {
			selfSelectionPieVO = stockAnalysisService
					.getInfluencePie(stockCodes,
							LocalDate.now().minusMonths(1), LocalDate.now());

			// 处理获取的数据
			Map<String, Double> sharedMap = selfSelectionPieVO.shareMap;
			Map<String, StockInfoVO> stockMap = selfSelectionPieVO.stockMap;
			Set<String> keySet = sharedMap.keySet();
			String stockName = null;
			double shareValue = 0;
			for (String key : keySet) {
				shareValue = sharedMap.get(key);
				stockName = stockMap.get(key).getName();
				// 将数据添加进界面内和PieChartBuilder中的Map，如果已有该数据就不再添加
				if (selfData.get(stockName) == null) {
					selfData.put(stockName, shareValue);
				} else {
					selfData.replace(stockName, shareValue);
				}
				// 构建添加进pieChart的Map
				HashMap<String, Double> addedData = new HashMap<String, Double>();
				addedData.put(stockName, shareValue);
				if (isIndustryRunning) {
					isIndustryRunning = false;
					pieChart = new PieChartBuilder(pieChart).setData(selfData)
							.build();
				} else {
					pieChart = new PieChartBuilder(pieChart).setData(selfData)
							.build();
				}
			}

		} catch (NetworkConnectionException e1) {
			new NetExceptionTips();
			e1.printStackTrace();
		}
	}

	private void setLineChart(StockInfoVO stock)
			throws NetworkConnectionException {

		Collection<PointVO> pointVOs = stockAnalysisService.getTrendD(
				stock.getStockCode(), LocalDate.now().minusDays(7),
				LocalDate.now());

		XYChart.Series<String, Number> series = new XYChart.Series<>();

		Iterator<PointVO> iterator = pointVOs.iterator();

		while (iterator.hasNext()) {

			PointVO vo = iterator.next();

			series.getData().add(
					new XYChart.Data<String, Number>(vo.getDate().toString(),
							vo.y));

		}

		series.setName(stock.getName());
		lineChart.getData().add(series);

		seriesMap.put(stock.getStockCode(), series);

	}

	public void removeStock(ActionEvent actionEvent) {
		List<ContrastVO> selected = compareTableView.getSelectionModel()
				.getSelectedItems();

		for (ContrastVO contrastVO : selected) {
			selectedStocks.remove(contrastVO.stockInfoVO);

			String code = contrastVO.stockInfoVO.getStockCode();

			removeSelect(contrastVO.stockInfoVO.getName());

			XYChart.Series<String, Number> removeSeries = seriesMap.get(code);

			lineChart.getData().remove(removeSeries);
		}

		compareTableView.getItems().removeAll(selected);
	}

}
