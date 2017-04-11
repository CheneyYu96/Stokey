package org.xeon.stockey.ui.stockui.login;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.xeon.stockey.businessLogic.user.UserImpl;
import org.xeon.stockey.businessLogicService.userService.UserService;
import org.xeon.stockey.ui.main.MainApp;
import org.xeon.stockey.ui.stockui.GeneralController;
import org.xeon.stockey.ui.stockui.selection.StockListController;
import org.xeon.stockey.ui.utility.DragedScene;
import org.xeon.stockey.ui.utility.ExceptionTips;
import org.xeon.stockey.util.OperationResult;
import org.xeon.stockey.vo.UserVO;

import java.io.IOException;

/**
 * Created by yuminchen on 16/6/4.
 */
public class LoginController {
    private UserService user;

    private Stage stage;

    private GeneralController parentController;
    @FXML
    private TextField userName;

    @FXML
    private TextField passWord;

    @FXML
    private Label tipsLabel;

    @FXML
    private Label arrowLabel;

    private AnchorPane rootPanel;
    public static void launch(MainApp mainApp,GeneralController parentController) throws IOException{

        FXMLLoader loader = new FXMLLoader(LoginController.class.getResource("Login.fxml"));
        AnchorPane pane = loader.load();
        LoginController controller = loader.getController();
        controller.rootPanel = pane;
        controller.parentController = parentController;

        Stage loginStage = new Stage();
        loginStage.initModality(Modality.WINDOW_MODAL);
        loginStage.initStyle(StageStyle.UNDECORATED);
        loginStage.initOwner(mainApp.getPrimaryStage());

        Scene newScene = new Scene(pane);
        loginStage.setScene(newScene);
        DragedScene.enableDrag(newScene, loginStage);
        loginStage.show();

        controller.setStage(loginStage);
    }

    private void setStage(Stage stage){
        this.stage = stage;
    }

    @FXML
    private void initialize(){
    }

    @FXML
    private void exit(){
        stage.close();
    }

    @FXML
    private void login(){
        if(user==null){
            user = new UserImpl();
        }
        OperationResult<UserVO> result = user.logIn(userName.getText(),passWord.getText());
        System.out.println(result.success+"----"+result.getBundle());
        if(result.success){
            parentController.setLoginInfo(userName.getText());
            exit();
        }
        else {
            new ExceptionTips(result.reason);
        }

    }

    @FXML
    private void signup(){
        AnchorPane loginPanel = (AnchorPane) rootPanel.getChildren().get(0);
        AnchorPane signUpPanel = new AnchorPane();
        rootPanel.getChildren().set(0,signUpPanel);

        double height = loginPanel.getPrefHeight();
        double weight = loginPanel.getWidth();

        signUpPanel.setLayoutX(loginPanel.getLayoutX());
        signUpPanel.setLayoutY(loginPanel.getLayoutY());
        signUpPanel.setPrefHeight(height);
        signUpPanel.setPrefWidth(weight);

        Label mailLabel = new Label("验证邮箱");
        Label userIDLabel = new Label("用户名");
        Label passWord = new Label("输入密码");
        Label verifyPassWord = new Label("再次输入");
        setStyle(mailLabel,userIDLabel,passWord,verifyPassWord);

        mailText = new TextField();
        userIDText = new TextField();
        passwordText = new TextField();
        verifyText = new TextField();

        signUpButton = new Button("注册");
        cancelButton = new Button("取消");

        mailLabel.setLayoutX(0);
        mailLabel.setLayoutY(height/5);
        userIDLabel.setLayoutX(0);
        userIDLabel.setLayoutY(height*2/5);
        passWord.setLayoutX(0);
        passWord.setLayoutY(height*3/5);
        verifyPassWord.setLayoutX(0);
        verifyPassWord.setLayoutY(height*4/5);

        mailText.setLayoutX(weight/3);
        mailText.setLayoutY(height/5);
        userIDText.setLayoutX(weight/3);
        userIDText.setLayoutY(height*2/5);
        passwordText.setLayoutX(weight/3);
        passwordText.setLayoutY(height*3/5);
        verifyText.setLayoutX(weight/3);
        verifyText.setLayoutY(height*4/5);

        signUpButton.setLayoutX(weight/4);
        signUpButton.setLayoutY(height);
        cancelButton.setLayoutX(weight*3/4);
        cancelButton.setLayoutY(height);

        signUpButton.setOnMouseClicked(handle->registerAccount());
        cancelButton.setOnMouseClicked(handle->exit());

        signUpPanel.getChildren().addAll(
                mailLabel,passWord,verifyPassWord,userIDLabel,mailText,userIDText,passwordText,verifyText,cancelButton,signUpButton);

        tipsLabel.setVisible(false);
        arrowLabel.getStyleClass().clear();
        arrowLabel.getStyleClass().addAll("label-arrow-left");
        // change the click event
        arrowLabel.setOnMouseClicked(handle->
                {
                    tipsLabel.setVisible(true);
                    arrowLabel.getStyleClass().clear();
                    arrowLabel.getStyleClass().addAll("label-arrow-right");
                    rootPanel.getChildren().set(0,loginPanel);
                    arrowLabel.setOnMouseClicked(backHandle->signup());

                });

    }

    private void registerAccount(){
        if(user==null){
            user = new UserImpl();
        }
        if(!passwordText.getText().equals(verifyText.getText())){
            new ExceptionTips("两次密码不一致,请重新输入");
            return;
        }
        OperationResult result = user.registerAccount(mailText.getText(),userIDText.getText(),passwordText.getText());
        System.out.println(result.success+"----"+result.reason);
        if(result.success){
            new ExceptionTips("注册成功","请登录邮箱认证");
            exit();
        }
        else {
            new ExceptionTips(result.reason);
        }
    }

    private void setStyle(Label... labels){
        for(Label label : labels) {
            label.getStyleClass().addAll("label-tips");
        }
    }

    private TextField mailText ;
    private TextField userIDText ;
    private TextField passwordText ;
    private TextField verifyText ;

    private Button signUpButton;
    private Button cancelButton;
}
