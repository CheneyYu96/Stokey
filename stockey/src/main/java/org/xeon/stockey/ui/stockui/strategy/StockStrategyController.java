package org.xeon.stockey.ui.stockui.strategy;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import net.sf.json.JSONObject;
import org.xeon.stockey.businessLogic.stockAccess.FileBenchmarkSourceImpl;
import org.xeon.stockey.businessLogic.stockAccess.FileStockSourceImpl;
import org.xeon.stockey.businessLogic.stockAccess.StockAccessController;
import org.xeon.stockey.businessLogic.stockAccess.StockDailyDataFilterImpl;
import org.xeon.stockey.businessLogic.strategy.StrategyServiceStub;
import org.xeon.stockey.businessLogic.utility.NetworkConnectionException;
import org.xeon.stockey.businessLogicService.stockAccessService.StockDailyDataFilterService;
import org.xeon.stockey.businessLogicService.stockAccessService.StockSourceService;
import org.xeon.stockey.businessLogicService.strategyService.StrategyService;
import org.xeon.stockey.ui.main.MainApp;
import org.xeon.stockey.ui.stockui.GeneralInterface;
import org.xeon.stockey.ui.utility.*;
import org.xeon.stockey.vo.DailyDataVO;
import org.xeon.stockey.vo.JsonInfoVO;
import org.xeon.stockey.vo.StockInfoVO;
import org.xeon.stockey.vo.StockShowVO;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by yuminchen on 16/5/11.
 */
public class StockStrategyController extends GeneralInterface {

    private static StockStrategyController controller;

    private AnchorPane panel;

    private AnchorPane replacePanel;

    final private String BASIC_TIPS = "基础模式:通过鼠标选择买入卖出条件,系统自动模拟策略";

    final private String PRO_TIPS = "专业模式:通过编写python脚本,实现策略";

    private StrategyService strategy;
    @FXML
    private Label tipsLabel;

    public static Parent launch(MainApp mainApp) throws IOException {
        if(controller!=null){
            controller.panel.getChildren().set(0,controller.replacePanel);
            return controller.panel;
        }
        FXMLLoader loader = new FXMLLoader(
                StockStrategyController.class.getResource("StockStrategyEntrance.fxml"));
        Pane pane = loader.load();
        controller = loader.getController();
        controller.setMainApp(mainApp);
        controller.strategy = new StrategyServiceStub();
        if(pane instanceof AnchorPane) {
            controller.panel = (AnchorPane) pane;
            controller.replacePanel = (AnchorPane) controller.panel.getChildren().get(0);
        }
        return pane;
    }

    @FXML
    private void turn2Pro(){
        try {
            StockProStrategyController.launch(strategy,panel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void turn2Basic(){
        try {
            StockBasicStrategyController.launch(strategy,panel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showBasicTips(){
        tipsLabel.setText(BASIC_TIPS);
    }

    @FXML
    private void showProTips(){
        tipsLabel.setText(PRO_TIPS);
    }

    @FXML
    private void clearTips(){
        tipsLabel.setText("");
    }
}
