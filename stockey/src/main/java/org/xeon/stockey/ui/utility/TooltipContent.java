package org.xeon.stockey.ui.utility;

import javafx.scene.layout.GridPane;

/**
 * Created by yuminchen on 16/4/10.
 */
public class TooltipContent extends GridPane {

    public TooltipContent(String styleString) {
        this.getStylesheets().add(styleString);
//        this.getStylesheets().addAll(getClass().getResource(styleString).toExternalForm());
        initialLabel();
        initStyleAndLocated();
    }
    protected void initialLabel(){}

    protected void initStyleAndLocated(){}

}
