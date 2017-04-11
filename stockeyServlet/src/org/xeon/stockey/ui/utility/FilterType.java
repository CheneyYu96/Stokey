package org.xeon.stockey.ui.utility;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public enum FilterType {
	DAILYDATA;

	ObservableList<String> types;

	FilterType() {
		types = FXCollections.observableArrayList();
		types.addAll("日期", "最高价", "最低价", "开盘价", "收盘价", "交易量", "交易金额");
	}

	public ObservableList<String> getTypes() {
		return types;
	}
}
