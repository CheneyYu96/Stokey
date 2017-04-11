package org.xeon.stockey.ui.stockui.strategy;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import org.xeon.stockey.vo.JsonInfoVO;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * Created by yuminchen on 16/6/1.
 */
public class StrategyResultController {
    @FXML
    private LineChart<String,Number> lineChart;

    @FXML
    private Button returnButton;

    @FXML
    private void initialize(){
        returnButton.setOnMouseClicked(StockProStrategyController::setBack);
    }

    public static Pane showResultChart(List<JsonInfoVO> jsonInfoVOs){
        FXMLLoader loader = new FXMLLoader(StrategyResultController.class.getResource("StockResult.fxml"));
        AnchorPane pane = null;
        try {
            pane = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        StrategyResultController controller = loader.getController();
      
        XYChart.Series<String, Number> series = new XYChart.Series<>();

        Iterator<JsonInfoVO> iterator = jsonInfoVOs.iterator();
        while (iterator.hasNext()){
            JsonInfoVO jsonInfoVO = iterator.next();

            series.getData().add(new XYChart.Data<String, Number>(
                    jsonInfoVO.getDate().toString(),jsonInfoVO.getTotalMoney()));
        }
        controller.lineChart.getData().add(series);
        return pane;
    }

}
