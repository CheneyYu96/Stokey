package org.xeon.stockey.ui.stockui.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.StackPane;

import org.apache.commons.collections.map.LinkedMap;
import org.xeon.stockey.ui.stockui.graph.XYChartBuilder.ToolTipType;
import org.xeon.stockey.ui.utility.ChartUtil;
import org.xeon.stockey.ui.utility.FilePath;
import org.xeon.stockey.vo.KdjVO;
import org.xeon.stockey.vo.MacdVO;
import org.xeon.stockey.vo.PointVO;
import org.xeon.stockey.vo.RsiVO;
import org.xeon.stockey.vo.VolumeVO;

/**
 * K线图下面的其他功能图(MACD、KDJ、RSI、销售量、销售额)
 * 
 * @author Alan
 *
 */
public class FunctionPanel extends StackPane {
	ArrayList<MacdVO> macdVOList;
	ArrayList<KdjVO> kdjVOList;
	ArrayList<RsiVO> rsiVOList;
	ArrayList<VolumeVO> volumeVOList;
	ArrayList<PointVO> turnoverVOList;
	LineChart<String, Number> difLineChart, kdjLineChart, rsiLineChart;
	BarChart<String, Number> oscBarChart, volumeBarChart, turnoverBarChart;
	boolean kdjRun = false, rsiRun = false, volumeRun = false, turnoverRun;

	/**
	 * 设置MACD线的data
	 * 
	 * @param macdVOList
	 */
	public void setMACDData(ArrayList<MacdVO> macdVOList) {
		this.macdVOList = macdVOList;
		initialMACDChart();
	}

	public void setKDJData(ArrayList<KdjVO> kdjVOList) {
		this.kdjVOList = kdjVOList;
		initialKDJChart();
	}

	public void setRSIData(ArrayList<RsiVO> rsiVOList) {
		this.rsiVOList = rsiVOList;
		initialRSIChart();
	}

	public void setVolumeData(ArrayList<VolumeVO> volumeVOList) {
		this.volumeVOList = volumeVOList;
		initialVolumeChart();
	}

	public void setTurnoverData(ArrayList<PointVO> turnoverVOList) {
		this.turnoverVOList = turnoverVOList;
		initialTurnoverChart();
	}

	/**
	 * 设置MACD表的可见度
	 * 
	 * @param isShown
	 */
	public void setMACDVisibility(boolean isShown) {
		if (difLineChart != null) {
			difLineChart.setVisible(isShown);
		}
		if (oscBarChart != null) {
			oscBarChart.setVisible(isShown);
		}
	}

	/**
	 * 设置KDJ表的可见度
	 * 
	 * @param isShown
	 */
	public void setKDJVisibility(boolean isShown) {
		if (kdjLineChart != null) {
			kdjLineChart.setVisible(isShown);
		}
	}

	public boolean isKDJRunned() {
		return kdjRun;
	}

	public void setRSIVisibility(boolean isShown) {
		if (rsiLineChart != null) {
			rsiLineChart.setVisible(isShown);
		}
	}

	public boolean isRSIRunned() {
		return rsiRun;
	}

	public void setVolumeVisibility(boolean isShown) {
		if (volumeBarChart != null) {
			volumeBarChart.setVisible(isShown);
		}
	}

	public boolean isVolumeRunned() {
		return volumeRun;
	}

	public void setTurnoverVisibility(boolean isShown) {
		if (turnoverBarChart != null) {
			turnoverBarChart.setVisible(isShown);
		}
	}

	public boolean isTurnoverRunned() {
		return turnoverRun;
	}

	/**
	 * 初始化数据及显示
	 */
	@SuppressWarnings("unchecked")
	private void initialMACDChart() {
		Map<String, Number> difData = new LinkedHashMap<String, Number>();
		Map<String, Number> demData = new LinkedHashMap<String, Number>();
		Map<String, Number> oscData = new LinkedHashMap<String, Number>();
		double lowerbound = 0, upperbound = 0, tickUnit = 0;
		for (MacdVO vo : macdVOList) {
			String date = vo.getDate().toString();
			double dif = vo.getDif(), dem = vo.getDem(), osc = vo.getOsc();
			difData.put(date, dif);
			demData.put(date, dem);
			oscData.put(date, osc);
			upperbound = ChartUtil.findMax(upperbound,
					ChartUtil.findMax(dif, dem, osc));
			lowerbound = ChartUtil.findMin(lowerbound,
					ChartUtil.findMin(dif, dem, osc));
		}
		upperbound = setUpperBound(upperbound);
		lowerbound = setLowerBound(lowerbound);
		tickUnit = setTickUnit(upperbound, lowerbound);

		if (difLineChart != null) {
			removeAllCharts();
		}

		difLineChart = new LineChartBuilder(ChartUtil.createYaxis(lowerbound,
				upperbound, tickUnit)).setToolTip(ToolTipType.EXPLICIT)
				.addSeriesData("dif", difData).addSeriesData("dem", demData)
				.build();

		oscBarChart = new BarChartBuilder(ChartUtil.createYaxis(lowerbound,
				upperbound, tickUnit)).setToolTip(ToolTipType.EXPLICIT)
				.addSeriesData("", oscData).build();
		oscBarChart.setBarGap(0.5);

		layerCharts(oscBarChart, difLineChart);

	}

	@SuppressWarnings("unchecked")
	private void initialKDJChart() {
		kdjRun = true;
		Map<String, Number> kData = new LinkedHashMap<String, Number>();
		Map<String, Number> dData = new LinkedHashMap<String, Number>();
		Map<String, Number> jData = new LinkedHashMap<String, Number>();
		double lowerbound = 0, upperbound = 0, tickUnit = 0;
		for (KdjVO vo : kdjVOList) {
			String date = vo.getDate().toString();
			double kValue = vo.kValue, dValue = vo.dValue, jValue = vo.jValue;
			kData.put(date, kValue);
			dData.put(date, dValue);
			jData.put(date, jValue);
			upperbound = ChartUtil.findMax(upperbound,
					ChartUtil.findMax(kValue, dValue, jValue));
			lowerbound = ChartUtil.findMin(lowerbound,
					ChartUtil.findMin(kValue, dValue, jValue));
		}
		upperbound = setUpperBound(upperbound);
		lowerbound = setLowerBound(lowerbound);
		tickUnit = setTickUnit(upperbound, lowerbound);

		kdjLineChart = new LineChartBuilder(ChartUtil.createYaxis(lowerbound,
				upperbound, tickUnit)).setToolTip(ToolTipType.EXPLICIT)
				.addSeriesData("k", kData).addSeriesData("d", dData)
				.addSeriesData("j", jData).build();
		layerCharts(kdjLineChart);
	}

	/**
	 * 初始化RSI图表
	 */
	private void initialRSIChart() {
		rsiRun = true;
		Map<String, Number> r6Data = new LinkedHashMap<String, Number>();
		Map<String, Number> r12Data = new LinkedHashMap<String, Number>();
		Map<String, Number> r24Data = new LinkedHashMap<String, Number>();
		double lowerbound = 0, upperbound = 0, tickUnit = 0;
		for (RsiVO vo : rsiVOList) {
			String date = vo.getDate().toString();
			double rsi6 = vo.rsi6, rsi12 = vo.rsi12, rsi24 = vo.rsi24;
			r6Data.put(date, rsi6);
			r12Data.put(date, rsi12);
			r24Data.put(date, rsi24);
			upperbound = ChartUtil.findMax(upperbound,
					ChartUtil.findMax(rsi6, rsi12, rsi24));
			lowerbound = ChartUtil.findMin(lowerbound,
					ChartUtil.findMin(rsi6, rsi12, rsi24));
		}
		upperbound = setUpperBound(upperbound);
		lowerbound = setLowerBound(lowerbound);
		tickUnit = setTickUnit(upperbound, lowerbound);

		rsiLineChart = new LineChartBuilder(ChartUtil.createYaxis(lowerbound,
				upperbound, tickUnit)).setToolTip(ToolTipType.EXPLICIT)
				.addSeriesData("r6", r6Data).addSeriesData("r12", r12Data)
				.addSeriesData("r24", r24Data).build();
		layerCharts(rsiLineChart);

	}

	private void initialVolumeChart() {
		volumeRun = true;
		Map<String, Number> volumeData = new LinkedHashMap<String, Number>();
		double lowerbound = 0, upperbound = 0, tickUnit = 0;
		for (VolumeVO vo : volumeVOList) {
			String date = vo.getDate().toString();
			double volume = vo.volume;
			volumeData.put(date, volume);
			upperbound = ChartUtil.findMax(upperbound,
					ChartUtil.findMax(volume));
			lowerbound = ChartUtil.findMin(lowerbound,
					ChartUtil.findMin(volume));
		}
		upperbound = setUpperBound(upperbound);
		lowerbound = setLowerBound(lowerbound);
		tickUnit = setTickUnit(upperbound, lowerbound);

		volumeBarChart = new BarChartBuilder(ChartUtil.createYaxis(lowerbound,
				upperbound, tickUnit)).setToolTip(ToolTipType.EXPLICIT)
				.addSeriesData("volume", volumeData).build();
		layerCharts(volumeBarChart);
	}

	public void initialTurnoverChart() {
		turnoverRun = true;
		Map<String, Number> turnoverData = new LinkedHashMap<String, Number>();
		double lowerbound = 0, upperbound = 0, tickUnit = 0;
		for (PointVO vo : turnoverVOList) {
			String date = vo.getDate().toString();
			double volume = vo.y;
			turnoverData.put(date, volume);
			upperbound = ChartUtil.findMax(upperbound,
					ChartUtil.findMax(volume));
			lowerbound = ChartUtil.findMin(lowerbound,
					ChartUtil.findMin(volume));
		}
		upperbound = setUpperBound(upperbound);
		lowerbound = setLowerBound(lowerbound);
		tickUnit = setTickUnit(upperbound, lowerbound);

		turnoverBarChart = new BarChartBuilder(ChartUtil.createYaxis(
				lowerbound, upperbound, tickUnit))
				.setToolTip(ToolTipType.CUSTOM)
				.setCustomToolTip("", "", "", "%")
				.addSeriesData("turnover", turnoverData).build();
		layerCharts(turnoverBarChart);
	}

	@SuppressWarnings("unchecked")
	private void layerCharts(final XYChart<String, Number>... charts) {
		for (int i = 0; i < charts.length; i++) {
			if (i == 0) {
				configureOverlayChart(charts[i], true, true);
			} else {
				configureOverlayChart(charts[i], false, false);
			}
		}
		getChildren().addAll(charts);
	}

	private void configureOverlayChart(XYChart<String, Number> chart,
			boolean showHLine, boolean showVLine) {
		chart.setAlternativeRowFillVisible(false);
		chart.setAlternativeColumnFillVisible(false);
		chart.setHorizontalGridLinesVisible(showHLine);
		chart.setVerticalGridLinesVisible(showVLine);
		chart.getXAxis().setTickLabelsVisible(false);

		chart.getStylesheets().add(FilePath.OVERLAY_CSS);
	}

	private void removeAllCharts() {
		getChildren().removeAll(difLineChart, oscBarChart, rsiLineChart,
				kdjLineChart, volumeBarChart, turnoverBarChart);
	}

	private double setUpperBound(double upperbound) {
		if (upperbound > 0) {
			upperbound = 1.2 * upperbound;
		} else {
			upperbound = 0.8 * upperbound;
		}
		return upperbound;
	}

	private double setLowerBound(double lowerbound) {
		if (lowerbound > 0) {
			lowerbound = 0.8 * lowerbound;
		} else {
			lowerbound = 1.2 * lowerbound;
		}
		return lowerbound;
	}

	private double setTickUnit(Double upperbound, Double lowerbound) {
		return (upperbound - lowerbound) / 10;
	}

}
