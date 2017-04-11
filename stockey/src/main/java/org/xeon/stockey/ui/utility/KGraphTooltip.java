package org.xeon.stockey.ui.utility;

import javafx.scene.control.Label;

/**
 * Created by yuminchen on 16/4/10.
 */
public class KGraphTooltip extends  TooltipContent{
    private Label open ;

    private Label close;

    private Label high;

    private Label low ;

    private Label date;

    private Label openValue ;

    private Label closeValue;

    private Label highValue;

    private Label lowValue ;

    private Label dateValue;

    public KGraphTooltip(String styleString) {
        super(styleString);
    }

    @Override
    protected void initialLabel() {
        super.initialLabel();
        date = new Label("date");
        high = new Label("high:");
        low = new Label("low:");
        open = new Label("open");
        close = new Label("close");

        dateValue = new Label();
        highValue = new Label();
        lowValue = new Label();
        openValue = new Label();
        closeValue = new Label();
    }

    @Override
    protected void initStyleAndLocated() {
        super.initStyleAndLocated();

        open.getStyleClass().add("kgraph-tooltip-label");

        close.getStyleClass().add("kgraph-tooltip-label");

        high.getStyleClass().add("kgraph-tooltip-label");

        low.getStyleClass().add("kgraph-tooltip-label");


        setConstraints(date, 0, 0);

        setConstraints(dateValue, 1, 0);

        setConstraints(open, 0, 1);

        setConstraints(openValue, 1, 1);

        setConstraints(close, 0, 2);

        setConstraints(closeValue, 1, 2);

        setConstraints(high, 0, 3);

        setConstraints(highValue, 1, 3);

        setConstraints(low, 0, 4);

        setConstraints(lowValue, 1, 4);

        getChildren().addAll(date,dateValue,open, openValue, close, closeValue, high, highValue, low, lowValue);
    }

    public void update(double highV, double lowV, double openV, double closeV,String dateV) {
        highValue.setText(highV+"");
        lowValue.setText(lowV+"");
        openValue.setText(openV+"");
        closeValue.setText(closeV+"");
        dateValue.setText(dateV);
    }
}
