package org.xeon.stockey.ui.stockui.graph;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import org.xeon.stockey.ui.stockui.StockMarketController;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by yuminchen on 16/4/13.
 */
public class MAPanelController {

    private AnchorPane panel;

    @FXML
    private CheckBox ma5;

    @FXML
    private CheckBox ma10;

    @FXML
    private CheckBox ma20;

    @FXML
    private CheckBox ma30;
    /*
    map to all the checkboxes
     */


    private KGraphPanel parentPanel;


    public static AnchorPane launch(KGraphPanel parent) throws IOException{
        FXMLLoader loader = new FXMLLoader(
                MAPanelController.class.getResource("MAPanel.fxml"));

        AnchorPane pane = loader.load();

        MAPanelController controller = loader.getController();

        if(pane instanceof AnchorPane) {

            controller.panel = (AnchorPane) pane;

        }

        controller.ma5.setSelected(true);

        controller.ma10.setSelected(true);

        controller.ma20.setSelected(true);

        controller.ma30.setSelected(true);

        controller.parentPanel = parent;

        parent.getChildren().add(1,pane);

        return pane;

    }

    /**
     * call the parent panel to reset the ma line
     */
    public void setMA(){

        CheckBox[] checkBoxMap = {ma5,ma10,ma20,ma30};


        ArrayList<Integer> selectedMAList = new ArrayList<>();

        for(int i = 0;i<4 ;i++){

            if(true == checkBoxMap[i].
                    isSelected()){

                selectedMAList.add(i);

            }
        }

        Integer[] selectedMA = new Integer[selectedMAList.size()];

        for (int i = 0;i<selectedMAList.size();i++){

            selectedMA[i] = selectedMAList.get(i);

        }

        parentPanel.resetMA(selectedMA);


    }





}
