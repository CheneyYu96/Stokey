package org.xeon.stockey.ui.stockui.graph;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

/**
 * Created by yuminchen on 16/4/10.
 */
public class GeneralGraph extends Pane{

    protected GridPane topTips;

    protected GridPane rightTips;

    Label[] topLabels;

    protected  ToggleGroup group;


    /**
     * Creates a Pane layout.
     */
    public GeneralGraph(GridPane topTips, GridPane rightTips) {
        this.topTips = topTips;
        this.rightTips = rightTips;
    }

    /**
     * Creates a Pane layout.
     */
    public GeneralGraph() {
        topTips = new GridPane();

        rightTips = new GridPane();

        group = new ToggleGroup();


    }

    protected void setTopTips(double x , double y , double height,double width,String ... labels ){

        topTips.setLayoutX(x);

        topTips.setLayoutY(y);

        topTips.setPrefHeight(height);

        topTips.setPrefWidth(width);

        int size = labels.length;

        topLabels = new Label[size];

        for(int i = 0; i<size;i++){

            System.out.println(labels[i]);

            topLabels[i] = new Label(labels[i]);

            topLabels[i].getStyleClass().add("kgraph-tooltip-label");

            GridPane.setConstraints(topLabels[i],i,0);

        }


    }

    protected void addToggle(double x, double y, double width, double height,String ... value){

        int length = value.length;

        for(int i = 0 ; i<length;i++){

            RadioButton tmp = new RadioButton(value[i]);

            tmp.setToggleGroup(group);

            tmp.setSelected(true);

            tmp.setLayoutX(x+width*0.2);

            tmp.setLayoutY(y+i*height/length);

            this.getChildren().add(tmp);
        }

    }

    protected void setRightTips(){


    }


}
