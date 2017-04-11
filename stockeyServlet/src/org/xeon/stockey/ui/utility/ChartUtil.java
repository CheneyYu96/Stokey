package org.xeon.stockey.ui.utility;

import javafx.scene.chart.NumberAxis;

public class ChartUtil {

	public static NumberAxis createYaxis(double lowerbound, double upperbound,
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

	public static double findMax(double... nums) {
		double result = nums[0];
		for (double num : nums) {
			if (num > result) {
				result = num;
			}
		}
		return result;
	}

	public static double findMin(double... nums) {
		double result = nums[0];
		for (double num : nums) {
			if (num < result) {
				result = num;
			}
		}
		return result;
	}
}
