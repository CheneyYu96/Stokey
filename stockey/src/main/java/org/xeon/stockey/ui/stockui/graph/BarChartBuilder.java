package org.xeon.stockey.ui.stockui.graph;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javafx.collections.FXCollections;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;

/**
 * 柱状图构建器，创建默认BarChart，并对外提供监听设置，提示弹窗设置
 * 
 * @author Alan
 *
 */
public class BarChartBuilder extends XYChartBuilder {

	/**
	 * 字符串和数值类型的BarChart
	 */
	private BarChart<String, Number> chart;

	public BarChartBuilder() {
		// 初始化默认x、y轴
		super();

		// 初始化BarChart
		chart = new BarChart<String, Number>(xAxis, yAxis);
	}

	/**
	 * 自定义y轴
	 * 
	 * @param yAxis
	 */
	public BarChartBuilder(NumberAxis yAxis) {
		super(yAxis);
		chart = new BarChart<String, Number>(xAxis, yAxis);
	}

	public BarChartBuilder(BarChart<String, Number> chart) {
		this.chart = chart;
		if (chart.getXAxis() != null) {
			xAxis = (CategoryAxis) chart.getXAxis();
		}
		if (chart.getYAxis() != null) {
			yAxis = (NumberAxis) chart.getYAxis();
		}
		if (chart.getData() != null) {
			for (Series<String, Number> seriesData : chart.getData()) {
				series.put(seriesData.getName(), seriesData);
			}
		}
	}

	/**
	 * 构建BarChart
	 * 
	 */
	public BarChart<String, Number> build() {
		return chart;
	}

	/**
	 * 设置柱状图标题
	 * 
	 * @param title
	 */
	public BarChartBuilder setChartTitle(String title) {
		chart.setTitle(title);
		return this;
	}

	/**
	 * 设置X轴标签
	 * 
	 * @param label
	 */
	public BarChartBuilder setXAxisLabel(String label) {
		xAxis.setLabel(label);
		return this;
	}

	/**
	 * 设置Y轴标签
	 * 
	 * @param label
	 */
	public BarChartBuilder setYAxisLabel(String label) {
		yAxis.setLabel(label);
		return this;
	}

	/**
	 * 设置Y轴的标签前后缀
	 * 
	 * @param prefix
	 * @param suffix
	 */
	public BarChartBuilder setXTickLabelFormatter(String prefix, String suffix) {
		yAxis.setTickLabelFormatter(new NumberAxis.DefaultFormatter(yAxis,
				prefix, suffix));
		return this;
	}

	/**
	 * 设置X轴内容
	 * 
	 * @param xData
	 */
	public BarChartBuilder setXCategories(List<String> xData) {
		xAxis.setCategories(FXCollections.<String> observableArrayList(xData));
		return this;
	}

	/**
	 * 添加系列
	 * 
	 * @param seriesNames
	 *            系列名（可变参数）
	 * @return
	 */
	public BarChartBuilder addSeries(String... seriesNames) {
		for (String seriesName : seriesNames) {
			Series<String, Number> newSeries = new Series<String, Number>();
			newSeries.setName(seriesName);
			series.put(seriesName, newSeries);
			chart.getData().add(newSeries);
		}
		return this;
	}

	/**
	 * 删除系列
	 * 
	 * @param seriesNames
	 *            系列名（可变参数）
	 * @return
	 */
	public BarChartBuilder deleteSeries(String... seriesNames) {
		for (String seriesName : seriesNames) {
			chart.getData().remove(series.get(seriesName));
			series.remove(seriesName);
		}
		return this;
	}

	/**
	 * 添加系列的数据
	 * 
	 * @param seriesName
	 *            想要向哪个系列添加数据
	 * @param data
	 *            想要添加的数据，以Map<String,Number>形式传入
	 * @return
	 */
	public BarChartBuilder addSeriesData(String seriesName,
			Map<String, Number> data) {
		Series<String, Number> selectedSeries = series.get(seriesName);
		if (selectedSeries == null) {
			addSeries(seriesName);
			selectedSeries = series.get(seriesName);
		}

		Set<String> categories = data.keySet();
		for (String category : categories) {
			setData(selectedSeries, new XYChart.Data<String, Number>(category,
					data.get(category)));
		}
		return this;
	}

	/**
	 * 设置标签类型<br>
	 * <strong>注意：</strong>ToolTip的设置需要在放入数据之前，且要在最近一次AlBarChartBuilder构造中使用
	 * 
	 * @param toolTipType
	 * @return
	 */
	public BarChartBuilder setToolTip(ToolTipType toolTipType) {
		this.toolTipType = toolTipType;
		return this;
	}

	public BarChartBuilder setCustomToolTip(String xHead, String xEnd,
			String yHead, String yEnd) {
		customXHeader = xHead;
		customYHeader = yHead;
		customXEnd = xEnd;
		customYEnd = yEnd;
		return this;
	}

	/**
	 * <strong>[Attention]使用本方法必须采用界面控制器结构</strong>
	 * 
	 * @param controller
	 * @return
	 */
	public BarChartBuilder setOnClickAction(ControllerService controller) {
		this.controller = controller;
		return this;
	}

}
