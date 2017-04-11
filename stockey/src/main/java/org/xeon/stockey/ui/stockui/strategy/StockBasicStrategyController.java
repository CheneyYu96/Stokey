package org.xeon.stockey.ui.stockui.strategy;

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
import org.xeon.stockey.businessLogic.stockAccess.DBStockSourceImpl;
import org.xeon.stockey.businessLogic.stockAccess.FileBenchmarkSourceImpl;
import org.xeon.stockey.businessLogic.stockAccess.FileStockSourceImpl;
import org.xeon.stockey.businessLogic.stockAccess.StockDailyDataFilterImpl;
import org.xeon.stockey.businessLogic.strategy.StrategyServiceStub;
import org.xeon.stockey.businessLogic.utility.NetworkConnectionException;
import org.xeon.stockey.businessLogicService.stockAccessService.StockDailyDataFilterService;
import org.xeon.stockey.businessLogicService.stockAccessService.StockSourceService;
import org.xeon.stockey.businessLogicService.strategyService.StrategyService;
import org.xeon.stockey.ui.main.MainApp;
import org.xeon.stockey.ui.utility.ExceptionTips;
import org.xeon.stockey.ui.utility.Json2Data;
import org.xeon.stockey.ui.utility.NetExceptionTips;
import org.xeon.stockey.ui.utility.OtherUtil;
import org.xeon.stockey.vo.DailyDataVO;
import org.xeon.stockey.vo.JsonInfoVO;
import org.xeon.stockey.vo.StockInfoVO;
import org.xeon.stockey.vo.StockShowVO;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by yuminchen on 16/6/2.
 */
public class StockBasicStrategyController {

    /*
    static
     */
    private static StockBasicStrategyController controller;


    private Set<StockInfoVO> selectedStocks = new HashSet<>();

    private Collection<StockInfoVO> allStocks = new LinkedList<>();

    private StockSourceService stockSource = new DBStockSourceImpl();

    private StockSourceService benchmarkSource = new FileBenchmarkSourceImpl();

    private ObservableList<StockShowVO> stockShowVOs = FXCollections.observableArrayList();

    public DatePicker beginDate;

    public DatePicker endDate;

    public ComboBox<String> addChoiceBox;

    private TextField addTextField;

    public TextField inputMoney;

    public Label totalNumber;

    public Label shareNumber;

    public TableView<StockShowVO> stockTableView;

    public TableColumn<StockShowVO,String> stockNameColumn;

    public TableColumn<StockShowVO,String> stockCodeColumn;

    public TableColumn<StockShowVO,String> moneyColumn;

    public TableColumn<StockShowVO,String> shareNumberColumn;

    private StockSourceService stockSourceService;

    private StockDailyDataFilterService filter ;

    private StrategyService strategy;

    private double money;
    private int number;
    private boolean addable = true;


    private AnchorPane rootPanel;
    private AnchorPane subPanel;
    public static void launch(StrategyService strategy,AnchorPane rootPanel) throws IOException {

        if(controller!=null){
            rootPanel.getChildren().set(0,controller.subPanel);
            return;
        }
        FXMLLoader loader = new FXMLLoader(
                StockBasicStrategyController.class.getResource("StockBasicStrategy.fxml"));
        Pane pane = loader.load();

        controller = loader.getController();
        controller.
                rootPanel = rootPanel;
        controller.strategy = strategy;

        if(pane instanceof AnchorPane){
            controller.subPanel = (AnchorPane)pane;
        }
        rootPanel.getChildren().set(0,controller.subPanel);

    }

    @FXML
    private void getMoney(){
        if(!OtherUtil.isNumeric(inputMoney.getText())){
            new ExceptionTips("格式有误","请输入数字");
            addable = false;
            return;
        }
        addable = true;
        money = Double.parseDouble(inputMoney.getText());

        String stockCode = addTextField.getText();

        try {
            filter.setStockInfoVO(stockSourceService.getStockInfo(stockCode));
            Collection<DailyDataVO> dailyDataVOs = filter.filterDate(beginDate.getValue(),beginDate.getValue().plusDays(1)).getResult();
            double openPrice = dailyDataVOs.iterator().next().getOpen();
            number = (int)(money/openPrice);
            shareNumber.setText(number+"");
        } catch (NetworkConnectionException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
            new ExceptionTips("未选中股票");
        }


    }

    @FXML
    private void initialize(){
        initStock();
        stockSourceService = new DBStockSourceImpl();

        filter = new StockDailyDataFilterImpl(stockSourceService);
        strategy = new StrategyServiceStub();
        beginDate.setValue(LocalDate.now().minusDays(1));
        endDate.setValue(LocalDate.now());
        addTextField = new TextField();
        List<String> stringList = allStocks.stream()
                .map(o -> o.getStockCode() + " " + o.getName())
                .collect(Collectors.toList());

        addChoiceBox.setItems(FXCollections.observableList(stringList));
        addChoiceBox.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    String[] ss = newValue.split(" ");
                    addTextField.setText(ss[0]);
                });
        stockCodeColumn.setCellValueFactory(cell->new SimpleStringProperty(cell.getValue().stockCode));
        stockNameColumn.setCellValueFactory(cell->new SimpleStringProperty(cell.getValue().stockName));
        moneyColumn.setCellValueFactory(cell->new SimpleStringProperty(cell.getValue().money+""));
        shareNumberColumn.setCellValueFactory(cell->new SimpleStringProperty(cell.getValue().shareNumber+""));
        stockTableView.setItems(stockShowVOs);

    }

    private void initStock() {
        List<String> codes = new ArrayList<>();
        codes.add("sh600000");
        codes.add("sh600606");
        codes.add("sh600183");
        codes.add("sh600608");
        try {
            allStocks.addAll(stockSource.getStockInfos(codes));
//            allStocks.addAll(benchmarkSource.getAllStockInfo());
        } catch (NetworkConnectionException e) {
            new NetExceptionTips();
            e.printStackTrace();
        }
    }

    public void addStock(ActionEvent actionEvent) {
        getMoney();
        if(!addable){
            return;
        }
        // judge if in allStocks
        String stockCode = addTextField.getText();

        StockInfoVO stockInfoVO = null;
        boolean legal = false;
        for (StockInfoVO stock : allStocks) {
            if (stockCode.equals(stock.getStockCode())) {
                legal = true;
                stockInfoVO = stock;
                break;
            }
        }

        if (!legal) {
            new ExceptionTips("输入不符合股票代码规则");
            return;
        }

        // judge if already in selectedStocks
        if (selectedStocks.contains(stockInfoVO)) {
            new ExceptionTips("已经添加了相同的股票");
            return;
        }

        if(stockInfoVO != null){
            double nowMoney = Double.parseDouble(totalNumber.getText())+money;
            totalNumber.setText(nowMoney+"");
            StockShowVO vo  = new StockShowVO(number,money,stockInfoVO.getStockCode(),stockInfoVO.getName());
            stockShowVOs.add(vo);
        }

    }

    @FXML
    private void removeStock(){
        int index = stockTableView.getSelectionModel().getSelectedIndex();
        if(index<0){
            new ExceptionTips("未选中股票");
            return;
        }
        StockShowVO removeVO = stockTableView.getItems().get(index);
        stockShowVOs.remove(index);
        double removePrice = removeVO.money;
        totalNumber.setText(Double.parseDouble(totalNumber.getText())-removePrice+"");

    }

    @FXML
    private void clearMoneyNumber(){
        inputMoney.setText("");
        shareNumber.setText("0");
    }

    @FXML
    private void connect2Strategy(){
        new ExceptionTips("提示","该功能正在内测,敬请期待");
    }
}
