package org.xeon.stockey.ui.utility;

import java.time.LocalDate;

import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

/**
 * Created by yuminchen on 16/4/13.
 */
public class TimePanel extends AnchorPane {

	private LocalDate beginTime;

	private LocalDate endTime;

	private Label begin;

	private Label end;

	private DatePicker beginPicker;

	private DatePicker endPicker;

	private Button confirmButton;

	/**
	 * constructer to set its location
	 * 
	 * @param parent
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public TimePanel(Pane parent, double x, double y, double width,
			double height) {

		this(parent, x, y, width, height, LocalDate.of(2016, 3, 1), LocalDate
				.now());

	}

	/**
	 * constructer to set location and initialize the value of begin and end
	 * time
	 * 
	 * @param parent
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param begin
	 * @param end
	 */

	public TimePanel(Pane parent, double x, double y, double width,
			double height, LocalDate begin, LocalDate end) {

		this.setLayoutX(x);

		this.setLayoutY(y);

		this.setPrefHeight(height);

		this.setPrefWidth(width);

		this.beginTime = begin;

		this.endTime = end;

		initialLayout();

		// parent.getChildren().add(0,this);

	}

	private void initialLayout() {

		begin = new Label("起始时间");

		end = new Label("结束时间");

		beginPicker = new DatePicker();

		endPicker = new DatePicker();

		confirmButton = new Button("确认");

		setLocation(begin, this.getWidth() * 0.1, this.getHeight() * 0.2,
				this.getWidth() * 0.1, this.getHeight() * 0.6);

		setLocation(beginPicker, this.getWidth() * 0.3, this.getHeight() * 0.2,
				this.getWidth() * 0.1, this.getHeight() * 0.6);

		setLocation(end, this.getWidth() * 0.5, this.getHeight() * 0.2,
				this.getWidth() * 0.1, this.getHeight() * 0.6);

		setLocation(endPicker, this.getWidth() * 0.7, this.getHeight() * 0.2,
				this.getWidth() * 0.1, this.getHeight() * 0.6);

		setLocation(confirmButton, this.getWidth() * 0.9,
				this.getHeight() * 0.5, this.getWidth() * 0.08,
				this.getHeight() * 0.4);

	}

	/**
	 * set the element's location
	 * 
	 * @param control
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	private void setLocation(Control control, double x, double y, double width,
			double height) {

		control.setLayoutX(x);

		control.setLayoutY(y);

		control.setPrefWidth(width);

		control.setPrefHeight(height);

	}

}
