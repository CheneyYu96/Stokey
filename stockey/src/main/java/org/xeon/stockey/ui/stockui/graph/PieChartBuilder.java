package org.xeon.stockey.ui.stockui.graph;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;

import org.xeon.stockey.vo.PieVO;

/**
 * 饼状图构造器
 * 
 * @author Alan
 *
 */
public class PieChartBuilder {

	private PieChart chart;
	private ObservableList<Data> pieChartData = FXCollections
			.observableArrayList();
	private Map<String, Double> dataMap = new HashMap<String, Double>();

	/**
	 * 默认构造函数
	 */
	public PieChartBuilder() {
		chart = new PieChart();
	}

	/**
	 * 从FXML中加载chart，调用此构造函数
	 * 
	 * @param chart
	 */
	public PieChartBuilder(PieChart chart) {
		this.chart = chart;
	}

	/**
	 * 需要删除数据时需要同步已有数据，调用此构造函数
	 * 
	 * @param chart
	 * @param dataMap
	 */
	public PieChartBuilder(PieChart chart, Map<String, Double> dataMap) {
		this(chart);
		this.dataMap = dataMap;
	}

	/**
	 * 添加数据
	 * 
	 * @param data
	 * @return
	 */
	public PieChartBuilder addData(Map<String, Double> data) {
		for (String name : data.keySet()) {
			if (dataMap.get(name) == null) {
				dataMap.put(name, data.get(name));
				configureAddData(new Data(name, data.get(name)));
			}
		}
		return this;
	}

	/**
	 * 删除数据
	 * 
	 * @param key
	 * @return
	 */
	public PieChartBuilder removeData(final String key) {
		chart.getData().removeIf(new Predicate<Data>() {
			@Override
			public boolean test(Data t) {
				if (t.getName().equals(key)
						&& t.getPieValue() == dataMap.get(key)) {
					return true;
				} else {
					return false;
				}
			}
		});
		return this;
	}

	public PieChartBuilder addIndustryData(PieVO pieVO) {
		Map<String, Double> sharedMap = pieVO.shareMap;
		for (String key : sharedMap.keySet()) {
			double shareValue = sharedMap.get(key);
			Data data = new Data(key, shareValue);
			configureAddData(data);
		}
		return this;
	}

	/**
	 * 设置数据
	 * 
	 * @param dataMap
	 * @return
	 */
	public PieChartBuilder setData(Map<String, Double> dataMap) {
		this.dataMap = dataMap;
		deleteAllData();
		for (String name : dataMap.keySet()) {
			configureAddData(new Data(name, dataMap.get(name)));
		}
		return this;
	}

	/**
	 * 删除所有数据
	 * 
	 * @return
	 */
	public PieChartBuilder deleteAllData() {
		pieChartData = FXCollections.observableArrayList();
		chart.setData(pieChartData);
		return this;
	}

	/**
	 * 获得已有数据
	 * 
	 * @return
	 */
	public Map<String, Double> getData() {
		return dataMap;
	}

	/**
	 * 构建
	 * 
	 * @return
	 */
	public PieChart build() {
		return chart;
	}

	private Tooltip createToolTip(String data) {
		if (data.length() >= 5) {
			return new Tooltip(data.substring(0, 5) + "%");
		} else {
			return new Tooltip(data + "%");
		}
	}

	private void configureAddData(Data data) {
		chart.getData().add(data);
		final Tooltip tooltip = createToolTip(data.getPieValue()*100 + "");
		data.getNode().setOnMouseMoved(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				tooltip.show(data.getNode(), event.getScreenX() + 20,
						event.getScreenY());
			}
		});

		data.getNode().setOnMouseExited(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				tooltip.hide();
			}
		});
	}
}
