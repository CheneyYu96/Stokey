package org.xeon.stockey.ui.utility;

import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class SimpleDoubleProperty implements ObservableValue<Double> {

	Double value;

	public SimpleDoubleProperty(Double value) {
		this.value = value;
	}

	@Override
	public void addListener(InvalidationListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeListener(InvalidationListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addListener(ChangeListener<? super Double> listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeListener(ChangeListener<? super Double> listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public Double getValue() {
		if (value == 0.0) {
			return null;
		} else {
			return value;
		}
	}

}
