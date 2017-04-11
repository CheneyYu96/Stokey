package org.xeon.stockey.ui.stockui.strategy;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import net.sf.json.JSONObject;
import org.xeon.stockey.businessLogicService.strategyService.StrategyService;
import org.xeon.stockey.ui.utility.ExceptionTips;
import org.xeon.stockey.ui.utility.Json2Data;
import org.xeon.stockey.ui.utility.OtherUtil;
import org.xeon.stockey.vo.JsonInfoVO;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by yuminchen on 16/6/2.
 */
public class StockProStrategyController {
    private static StockProStrategyController controller;

    public DatePicker beginDate;

    public DatePicker endDate;

    public TextField totalMoney;

    public TextArea code;

    private StrategyService strategy;

    private AnchorPane rootPanel;

    private AnchorPane subPanel;

    @FXML
    private void initialize(){
        beginDate.setValue(LocalDate.now().minusDays(1));
        endDate.setValue(LocalDate.now());
    }

    public static void launch(StrategyService strategy,AnchorPane rootPanel) throws IOException{

        if(controller!=null){
            rootPanel.getChildren().set(0,controller.subPanel);
            return;
        }
        FXMLLoader loader = new FXMLLoader(
                StockProStrategyController.class.getResource("StockProStrategy.fxml"));

        Pane pane = loader.load();

        controller = loader.getController();
        controller.strategy = strategy;
        controller.rootPanel = rootPanel;

        if(pane instanceof AnchorPane) {
            controller.subPanel = (AnchorPane) pane;
        }
        rootPanel.getChildren().set(0,controller.subPanel);


    }

    @FXML
    private void connect2Strategy(){

        String input = totalMoney.getText();

        System.err.println(input);
        if(!OtherUtil.isNumeric(input)){
            new ExceptionTips("格式有误","请输入数字");
            return;
        }

        JSONObject jsonObject = strategy.runStrategy(code.getText(),beginDate.getValue(),endDate.getValue(),Double.parseDouble(input));
        List<JsonInfoVO> jsonInfoVOs = Json2Data.getJsonInfo(jsonObject);
        Pane pane = StrategyResultController.showResultChart(jsonInfoVOs);
        if(pane instanceof AnchorPane){
            rootPanel.getChildren().set(0,pane);
        }
    }

    static <T extends Event> void setBack(T event){
        controller.rootPanel.getChildren().set(0,controller.subPanel);
    }

}
