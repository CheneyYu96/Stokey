package org.xeon.stockey.ui.stockui.graph;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.chart.Axis;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

import org.xeon.stockey.ui.utility.FilePath;
import org.xeon.stockey.ui.utility.KGraphTooltip;
import org.xeon.stockey.vo.CandlestickVO;

/**
 * Created by yuminchen on 16/4/10.
 */
public class KGraphPanel extends GeneralGraph {

	private KChart kChart;

	private AnchorPane MAPanel;

	private String[] date;
	private double[] open;
	private double[] close;
	private double[] high;
	private double[] low;
	private double[] ma5;
	private double[] ma10;
	private double[] ma20;
	private double[] ma30;

	private double min;
	private double max;

	private boolean isma5;
	private boolean isma10;
	private boolean isma20;
	private boolean isma30;

	private Series<String,Number>[] lineSe;
	boolean[] isMamap = {isma5,isma10,isma20,isma30};


	public KGraphPanel(ArrayList<CandlestickVO> candlestickVOs) {

        if(candlestickVOs !=null) {

			initValue(candlestickVOs);

			initialChart();

			initMaPanel();

		}

        else {

            System.err.println("K chart VO is null");

        }

	}

	private void initMaPanel(){


		try {

			MAPanel = MAPanelController.launch(this);

		}
		catch (IOException e ){

			System.err.println("read file wrong");

		}




	}
	private void initValue(ArrayList<CandlestickVO> candlestickVOs ){

		int length = candlestickVOs.size();
		date = new String[length];
		open = new double[length];
		close = new double[length];
		low = new double[length];
		high = new double[length];
		ma5 = new double[length];
		ma10 = new double[length];
		ma20 = new double[length];
		ma30 = new double[length];

		isma5 = true;
		isma10 = true;
		isma20 = true;
		isma30 = true;

		lineSe = new Series[4];
		if(candlestickVOs.size()==0){
			return;
		}

		max = candlestickVOs.get(0).high;
		min = candlestickVOs.get(0).low;
		for (int index = 0; index < length; index++) {
			CandlestickVO vo = candlestickVOs.get(index);
			date[index] = vo.date.toString();
			open[index] = vo.open;
			close[index] = vo.close;
			low[index] = vo.low;
			if (min > low[index]) {
				min = low[index];
			}

			high[index] = vo.high;
			if (max < high[index]) {
				max = high[index];
			}
			ma5[index] = vo.ma5;
			ma10[index] = vo.ma10;
			ma20[index] = vo.ma20;
			ma30[index] = vo.ma30;

		}




	}

	@Override
	public void setPrefSize(double prefWidth, double prefHeight) {
		super.setPrefSize(prefWidth, prefHeight);
		kChart.setLayoutX(0);
        kChart.setLayoutY(0);
        kChart.setPrefWidth(prefWidth*8/9);
        kChart.setPrefHeight(prefHeight);


		MAPanel.setLayoutX(prefWidth*8/9);

		MAPanel.setLayoutY(0);

		MAPanel.setPrefWidth(prefWidth/9);

		MAPanel.setPrefHeight(prefHeight);
	}

	private NumberAxis createYaxis(double lowerbound, double upperbound,
			double tickUnit) {
		final NumberAxis axis = new NumberAxis(lowerbound, upperbound, tickUnit);
		axis.setPrefWidth(35);
		axis.setMinorTickCount(3);

		axis.setTickLabelFormatter(new NumberAxis.DefaultFormatter(axis) {
			@Override
			public String toString(Number object) {
				return String.format("%7.3f", object.floatValue());
			}
		});

		return axis;
	}

	private void initialChart() {

		CategoryAxis xAxis = new CategoryAxis();

		// set the upper and lower in the way that add 10 percents to the max
		// value
		// and minus 10 persents to the min value
		double offset = (max - min) / 10;

		NumberAxis yAxis = createYaxis(min - offset, max + offset, offset);

		kChart = new KChart(xAxis, yAxis);
		// setup chart

		Series<String, Number> series = new Series<>();

		for (int i = 0; i < date.length; i++) {

			series.getData().add(

					new XYChart.Data<String, Number>(

					date[i], open[i], new extraInfo(high[i], low[i], close[i],
							ma5[i], ma10[i], ma20[i], ma30[i])));

		}

		ObservableList<Series<String, Number>> data = kChart.getData();

		if (data == null) {

			data = FXCollections.observableArrayList(series);

			kChart.setData(data);

		} else {

			kChart.getData().add(series);

		}

		for (int i = 0; i < 4; i++) {

			Series<String, Number> lineSeries = new Series<>();

			lineSe[i] = lineSeries;

			kChart.getData().add(lineSeries);

		}
		this.getStylesheets().add(FilePath.KGRAPH_CSS);

		this.getChildren().add(0,kChart);


	}

	public void resetMA(Integer ... numbers){


		for (int i = 0; i <4 ;i++){

			isMamap[i] = false;

			kChart.seriesRemoved(lineSe[i]);
		}

		for(int i =0;i<numbers.length;i++){

			isMamap[numbers[i]] = true;


		}


		kChart.resetMALine();

	}

	class extraInfo {
		double high;

		double low;

		double close;

		double m5;

		double m10;

		double m20;

		double m30;

		public extraInfo(double high, double low, double close) {
			this.high = high;

			this.low = low;

			this.close = close;
		}

		public extraInfo(double high, double low, double close, double m5,
				double m10, double m20, double m30) {
			this.high = high;
			this.low = low;
			this.close = close;
			this.m5 = m5;
			this.m10 = m10;
			this.m20 = m20;
			this.m30 = m30;
		}
	}


	class KChart extends XYChart<String, Number> {

		private final Axis<String> numberAxis;

		private final Axis<Number> numberAxis2;

		/**
		 * Constructs a XYChart given the two axes. The initial content for the
		 * chart plot background and plot area that includes vertical and
		 * horizontal grid lines and fills, are added.
		 *
		 * @param numberAxis
		 *            X Axis for this XY chart
		 * @param numberAxis2
		 *            Y Axis for this XY chart
		 */
		public KChart(Axis<String> numberAxis, Axis<Number> numberAxis2) {
			super(numberAxis, numberAxis2);

			this.numberAxis = numberAxis;

			this.numberAxis2 = numberAxis2;
		}

		@Override
		protected void dataItemAdded(Series<String, Number> series,
				int itemIndex, Data<String, Number> item) {

			Node candle = creatNode(item);

			getPlotChildren().add(candle);
		}

		@Override
		protected void dataItemRemoved(Data<String, Number> item,
				Series<String, Number> series) {

		}

		@Override
		protected void dataItemChanged(Data<String, Number> item) {

		}

		@Override
		protected void seriesAdded(Series<String, Number> series,
				int seriesIndex) {

			if (seriesIndex == 0) {

				for (Data item : series.getData()) {

					Node kNode = creatNode(item);

					getPlotChildren().add(kNode);

				}

			}

			else {

				Path path = new Path();

				path.getStyleClass().setAll("kgraph-average-line",
						"average" + (seriesIndex - 1));

				series.setNode(path);

				getPlotChildren().add(path);

			}

		}


		@Override
		protected void seriesRemoved(Series<String, Number> series) {

			Path path = (Path) series.getNode();

			path.setVisible(false);
//			getPlotChildren().remove(path);

		}

		public  void resetMALine(){

			for(int i = 0;i<4;i++){

				if(isMamap[i] == true){

					Path path = (Path) lineSe[i].getNode();

					path.setVisible(true);
				}

			}


		}
		@Override
		protected void layoutPlotChildren() {
			if (getData() == null) {

				return;

			}

			// update candle positions

			Series<String, Number> series = getData().get(0);

			Iterator<Data<String, Number>> iter = getDisplayedDataIterator(series);

			while (iter.hasNext()) {

				Data<String, Number> item = iter.next();

				double x = getXAxis().getDisplayPosition(
						getCurrentDisplayedXValue(item));

				double y = getYAxis().getDisplayPosition(
						getCurrentDisplayedYValue(item));

				Node itemNode = item.getNode();

				extraInfo extra = (extraInfo) item.getExtraValue();
//
				if (itemNode instanceof KNode && extra != null) {

					KNode candle = (KNode) itemNode;

					double close = getYAxis().getDisplayPosition(extra.close);

					double high = getYAxis().getDisplayPosition(extra.high);

					double low = getYAxis().getDisplayPosition(extra.low);
					// calculate candle width

					double candleWidth = -1;

					// update candle

					candle.update(close - y, high - y, low - y, candleWidth);

					candle.updateTooltip(item.getYValue().doubleValue(),
							extra.close, extra.high, extra.low,
							item.getXValue());

					// position the candle

					candle.setLayoutX(x);

					candle.setLayoutY(y);

				}

                double[] MaMap = { extra.m5, extra.m10, extra.m20, extra.m30 };

				boolean[] isMamap = {isma5,isma10,isma20,isma30};


				for(int i = 1; i<5;i++){

                     Series<String,Number> lineSeries = getData().get(i);

                     Path path = null;


                     if(isMamap[i-1]&&lineSeries.getNode() instanceof Path){

                         path =(Path) lineSeries.getNode();

                         buildPath(path, x , getYAxis().getDisplayPosition(MaMap[i-1]) );

                     }

                 }

            }

		}

		/**
		 * @param path
		 *            a set of all path
		 */
		private void buildPath(Path path, double x, double value) {

			if (path.getElements().isEmpty()) {

				path.getElements().add(new MoveTo(x, value));

			} else {

				path.getElements().add(new LineTo(x, value));

			}

		}

		/**
		 * to add a new node auto called when add item
		 *
		 */

		private Node creatNode(Data data) {

			Node kNode = data.getNode();

			if (kNode == null) {

				kNode = new KNode();

				data.setNode(kNode);

			}

			return kNode;
		}
	}

	/**
     *
     */
	class KNode extends Group {
		private Line highLowLine = new Line();

		private Region bar = new Region();

		private boolean openAboveClose = true;

		private Tooltip tooltip = new Tooltip();

		/**
		 * node construct
		 */
		public KNode() {

			setAutoSizeChildren(false);

			getChildren().addAll(highLowLine, bar);
			// getChildren().addAll(bar);

			setStyleType();

			tooltip.setGraphic(new KGraphTooltip(FilePath.KGRAPH_CSS));

			Tooltip.install(bar, tooltip);

		}

		/**
		 * @param closeOffset
		 * @param highOffset
		 * @param lowOffset
		 * @param candleWidth
		 */
		public void update(double closeOffset, double highOffset,
				double lowOffset, double candleWidth) {

			openAboveClose = closeOffset > 0;

			setStyleType();

			highLowLine.setStartY(highOffset);

			highLowLine.setEndY(lowOffset);
			if (candleWidth == -1) {

				candleWidth = bar.prefWidth(-1);

			}

			candleWidth = bar.prefWidth(candleWidth);

			// System.out.println(highLowLine.getStyleClass());

			if (openAboveClose) {

				bar.resizeRelocate(-candleWidth / 2, 0, candleWidth,
						closeOffset);

			} else {

				bar.resizeRelocate(-candleWidth / 2, closeOffset, candleWidth,
						closeOffset * -1);

			}

		}

		/**
		 * @param open
		 * @param close
		 * @param high
		 * @param low
		 * @param date
		 */
		public void updateTooltip(double open, double close, double high,
				double low, String date) {

			KGraphTooltip tooltipContent = (KGraphTooltip) tooltip.getGraphic();

			tooltipContent.update(high, low, open, close, date);

		}

		private void setStyleType() {

			getStyleClass().setAll("kgraph-candle");

			highLowLine.getStyleClass().setAll("kgraph-line");

			bar.getStyleClass().setAll("kgraph-bar",
					openAboveClose ? "open-above-close" : "close-above-open");

		}
	}
}
