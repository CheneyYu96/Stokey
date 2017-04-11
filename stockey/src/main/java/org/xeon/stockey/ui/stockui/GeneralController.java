package org.xeon.stockey.ui.stockui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.xeon.stockey.ui.main.MainApp;
import org.xeon.stockey.ui.stockui.login.LoginController;
import org.xeon.stockey.ui.stockui.market.StockMarketController;
import org.xeon.stockey.ui.stockui.selection.StockListController;
import org.xeon.stockey.ui.stockui.strategy.StockStrategyController;

import java.io.IOException;

/**
 * Created by yuminchen on 16/6/16.
 */
public class GeneralController extends GeneralInterface{
    @FXML
    private Label market;
    @FXML
    private Label list;
    @FXML
    private Label stratege;
    @FXML
    private Label title;

    private static GeneralController controller;

    private BorderPane panel;

    private AnchorPane centerPane;

    private AnchorPane topPane;

    @FXML
    private Label welcomeLabel;

    @FXML
    private Label exitLabel;

    public static Pane launch(MainApp mainApp) throws IOException {

        if(controller!=null){
            return controller.panel;
        }
        FXMLLoader loader = new FXMLLoader(
                GeneralController.class.getResource("General.fxml"));

        Pane pane = loader.load();
        controller = loader.getController();
        controller.setMainApp(mainApp);

        if(pane instanceof BorderPane) {
            controller.panel = (BorderPane) pane;
        }
        controller.centerPane = (AnchorPane) controller.panel.getCenter();
        controller.topPane = (AnchorPane) controller.panel.getCenter();

        controller.setLabel((AnchorPane) controller.panel.getLeft());
        controller.jumpToMarket();

        return pane;

    }
    @FXML
    private void  initialize(){
        welcomeLabel.setVisible(false);


    }
    /*
    called in initialize ui in stockListController
 */
    public void setLabel(AnchorPane pane){
        VBox vBox = (VBox)pane.getChildren().get(0);
        market = (Label) vBox.getChildren().get(0);
        list = (Label) vBox.getChildren().get(1);
        stratege = (Label) vBox.getChildren().get(2);
    }

    public void setTitle(Node label){
        if(label instanceof Label){
            title = (Label)label;
        }
    }

    public void closeStage() {
        mainApp.getPrimaryStage().close();
        System.exit(0);
    }

    public void minimizeStage() {
        mainApp.getPrimaryStage().toBack();
    }

    /*
     * 导航跳转
     * 后续可增加
     */
    public void jumpToMarket() {
        Parent pane = null;

        try {
            pane = StockMarketController.launch(mainApp);
        } catch (IOException e) {
            System.err.println("market fail to open");
            e.printStackTrace();
        }

        panel.setCenter(pane);
        clearAll();
        market.getStyleClass().addAll("label-market-selected");
        list.getStyleClass().addAll("label-selection","special2");
        stratege.getStyleClass().addAll("label-schemes","special3");
        title.getStyleClass().add("label-market");

    }

    public void jumpToSelection(){
        clearAll();
        market.getStyleClass().addAll("label-market","special1");
        list.getStyleClass().addAll("label-selection-selected");
        stratege.getStyleClass().addAll("label-schemes","special3");
        title.getStyleClass().add("label-selection");

//        Parent pane = null;
        FXMLLoader loader = new FXMLLoader(GeneralController.class.getResource("Loading.fxml"));
        try {
            Pane pane  = loader.load();
            panel.setCenter(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }
        toSelection();

//        try {
//            pane = StockListController.launchSingle(mainApp);
//        } catch (IOException e) {
//            System.err.println("market fail to open");
//            e.printStackTrace();
//        }
//        panel.setCenter(pane);


    }

    private void toSelection(){
        Parent pane = null;
        try {
            pane = StockListController.launchSingle(mainApp);
        } catch (IOException e) {
            System.err.println("market fail to open");
            e.printStackTrace();
        }
        panel.setCenter(pane);

    }

    public void jumpToStrategy() {
        Parent pane = null;
        try {
            pane = StockStrategyController.launch(mainApp);
        } catch (IOException e) {
            System.err.println("strategy fail to open");
            e.printStackTrace();
        }
        panel.setCenter(pane);

        clearAll();
        market.getStyleClass().addAll("label-market","special1");
        list.getStyleClass().addAll("label-selection","special2");
        stratege.getStyleClass().addAll("label-schemes-selected");
        title.getStyleClass().add("label-schemes");

    }

    public void clearAll(){
        market.getStyleClass().clear();
        list.getStyleClass().clear();
        stratege.getStyleClass().clear();
        title.getStyleClass().clear();
    }

    @FXML
    private void login(){
        try {
            LoginController.launch(mainApp,this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setLoginInfo(String userName){
        welcomeLabel.setVisible(true);
        welcomeLabel.setText("欢迎您 : "+userName);
        exitLabel.setText("退出");
        exitLabel.setOnMouseClicked(handle->exit());
    }

    private void exit(){
        welcomeLabel.setText("");
        welcomeLabel.setVisible(false);
        exitLabel.setText("登录/注册");
        exitLabel.setOnMouseClicked(handle->login());
    }
}
