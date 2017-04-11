package org.xeon.stockey.ui.stockui;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

/**
 * Created by yuminchen on 16/6/19.
 */
public class LoadingThread extends Thread {

    private BorderPane borderPane;

    public LoadingThread(BorderPane borderPane) {
        this.borderPane = borderPane;
    }

    @Override
    public void run() {
        FXMLLoader loader = new FXMLLoader(LoadingThread.class.getResource("Loading.fxml"));
        try {
            Pane pane = loader.load();
            borderPane.setCenter(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
