package org.xeon.stockey.ui.stockui.graph;

import java.util.HashMap;

import javafx.event.EventHandler;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;

public abstract class XYChartBuilder {

	/**
	 * X轴
	 */
	CategoryAxis xAxis;

	/**
	 * Y轴
	 */
	NumberAxis yAxis;

	/**
	 * 系列及数据
	 */
	HashMap<String, XYChart.Series<String, Number>> series = new HashMap<String, XYChart.Series<String, Number>>();;

	/**
	 * 标签类型
	 */
	ToolTipType toolTipType = ToolTipType.NORMAL;

	/**
	 * 自定义tooltip的开头
	 */
	String customXHeader = "", customYHeader = "", customXEnd = "",
			customYEnd = "";

	/**
	 * 界面跳转控制
	 */
	ControllerService controller;

	public XYChartBuilder() {
		// 初始化默认x、y轴
		xAxis = new CategoryAxis();
		yAxis = new NumberAxis();
	}

	public XYChartBuilder(NumberAxis yAxis) {
		this();
		this.yAxis = yAxis;
	}

	/**
	 * 构建特定类型的XY轴图表
	 * 
	 * @return
	 */
	public abstract XYChart<String, Number> build();

	/**
	 * 弹窗类型
	 * 
	 * @author Alan
	 *
	 */
	public enum ToolTipType {
		/**
		 * 普通弹窗，包含Y轴数据
		 */
		NORMAL,
		/**
		 * 详尽信息，包含X轴和Y轴数据
		 */
		EXPLICIT,
		/**
		 * 自定义信息格式，需要配合使用
		 * {@link AlBarChartBuilder#setCustomToolTip(String, String)}
		 */
		CUSTOM;
	}

	/**
	 * 添加数据并设置每一条数据的ToolTip显示监听
	 * 
	 * @param series
	 * @param data
	 */
	void setData(final XYChart.Series<String, Number> series,
			final XYChart.Data<String, Number> data) {
		series.getData().add(data);
		final Tooltip tooltip = createTooltip(data.getXValue(),
				data.getYValue() + "");

		data.getNode().setOnMouseMoved(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				tooltip.show(data.getNode(), event.getScreenX() + 20,
						event.getScreenY());
			}
		});
		data.getNode().setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (controller != null) {
					controller.jump(series.getName() + " " + data.getXValue(),
							data.getYValue() + "");
				}
			}
		});
		data.getNode().setOnMouseExited(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				tooltip.hide();
			}
		});
	}

	/**
	 * 根据ToolTipType来创建不同类型的ToolType
	 * 
	 * @param xData
	 * @param yData
	 * @return
	 */
	Tooltip createTooltip(String xData, String yData) {
		switch (toolTipType) {
		case NORMAL:
			return new Tooltip(yData);
		case EXPLICIT:
			if (yData.length() >= 5) {
				return new Tooltip(xData + "\n" + yData);
			} else {
				return new Tooltip(xData + "\n" + yData);
			}
		case CUSTOM:
			return new Tooltip(customXHeader + xData + customXEnd + "\n"
					+ customYHeader + yData + customYEnd);
		default:
			return new Tooltip(yData);
		}
	}

	/**
	 * 界面跳转控制接口，需要实现服务中的跳转方法
	 * 
	 * @author Alan
	 *
	 */
	public interface ControllerService {

		/**
		 * 界面跳转
		 * 
		 * @param xData
		 *            x轴数据
		 * @param yData
		 *            y轴数据
		 */
		public void jump(String xData, String yData);

	}
}
