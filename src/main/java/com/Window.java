package com;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.market.Candlestick;
import com.binance.api.client.domain.market.CandlestickInterval;
import com.model.BitcoinClass;
import com.model.EthereumClass;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.*;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Window extends Application {

    private BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance("API CODE FROM BINANCE", "SECRET CODE FROM BINANCE");
    private BinanceApiRestClient client = factory.newRestClient();
    private LocalTime newDay = LocalTime.of(1, 0);
    private Candlestick BTCDaily = null;
    private Candlestick ETHDaily = null;
    private List<Candlestick> BTCCandlesticks;
    private List<Candlestick> ETHCandlesticks;
    public void run() {
        launch();
    }

    @Override
    public void start(Stage primaryStage){



        if (ping()){

            BTCCandlesticks = client.getCandlestickBars("BTCUSDT", CandlestickInterval.DAILY);
            BTCDaily = BTCCandlesticks.get(BTCCandlesticks.size() - 1);

            ETHCandlesticks = client.getCandlestickBars("ETHUSDT", CandlestickInterval.DAILY);
            ETHDaily = ETHCandlesticks.get(ETHCandlesticks.size() - 1);

        }

        EthereumClass ethereumClass = new EthereumClass(client, ETHDaily);

        BitcoinClass bitcoinClass = new BitcoinClass(client, BTCDaily);

        ExecutorService updatePrices = Executors.newCachedThreadPool();

        updatePrices.submit(() -> {

            while (true) {
                if (ping()) {
                    if (LocalTime.now() == newDay) {
                        BTCCandlesticks = client.getCandlestickBars("BTCUSDT", CandlestickInterval.DAILY);
                        BTCDaily = BTCCandlesticks.get(BTCCandlesticks.size() - 1);
                        bitcoinClass.setBTCDaily(BTCDaily);

                        ETHCandlesticks = client.getCandlestickBars("ETHUSDT", CandlestickInterval.DAILY);
                        ETHDaily = ETHCandlesticks.get(ETHCandlesticks.size() - 1);
                        ethereumClass.setETHDaily(ETHDaily);

                        Thread.sleep(60000);

                    }
                }
            }



        });

        updatePrices.submit(()->{

            while (true) {
                bitcoinClass.setBTCPrice();
                ethereumClass.setETHPrice();
            }

        });


        primaryStage.setWidth(310);
        primaryStage.setHeight(250);
//        primaryStage.setY(-260);
//        primaryStage.setX(-300);
        primaryStage.setY(0);
        primaryStage.setX(0);
        primaryStage.initStyle(StageStyle.UTILITY);
        primaryStage.setAlwaysOnTop(false);
        primaryStage.setOpacity(0);
        primaryStage.setResizable(false);
        primaryStage.show();

        Stage mainStage = new Stage();
        mainStage.initOwner(primaryStage);
        mainStage.initStyle(StageStyle.UNDECORATED);

        HBox BTCHbox = new HBox();
        BTCHbox.setAlignment(Pos.CENTER_LEFT);

        Label BTCNameLabel = new Label();
        BTCNameLabel.setStyle(
                "fx-aligment: center;" +
                        "-fx-text-aligment: right;" +
                        "-fx-font-family: Impact;" +
                        "-fx-font-size: 50;" +
                        "-fx-text-fill: Grey;"
        );
        BTCNameLabel.setText("BTC:  ");

        VBox BTCInfoVBox = new VBox();

        Label BTCLabelPrice = new Label();
        BTCLabelPrice.setStyle(
                "-fx-text-aligment: right;" +
                        "-fx-font-family: Impact;" +
                        "-fx-font-size: 50;" +
                        "-fx-text-fill: Grey;"
        );
        Label BTCLabelPercent = new Label();
        BTCLabelPercent.setStyle(
                "-fx-text-aligment: right;" +
                        "-fx-font-family: Impact;" +
                        "-fx-font-size: 50;" +
                        "-fx-text-fill: Grey;"
        );

        BTCInfoVBox.getChildren().addAll(BTCLabelPrice, BTCLabelPercent);

        BTCHbox.getChildren().addAll(BTCNameLabel, BTCInfoVBox);

        HBox ETHHbox = new HBox();
        ETHHbox.setAlignment(Pos.CENTER_LEFT);

        Label ETHNameLabel = new Label();
        ETHNameLabel.setStyle(
                "-fx-aligment:center;" +
                        "-fx-text-aligment: right;" +
                        "-fx-font-family:Impact;" +
                        "-fx-font-size: 50;" +
                        "-fx-text-fill: Grey;"
        );
        ETHNameLabel.setText("ETH:  ");

        VBox ETHinfoVBox = new VBox();

        Label ETHLabelPrice = new Label();
        ETHLabelPrice.setStyle(
                "-fx-aligment:center;" +
                        "-fx-text-aligment: right;" +
                        "-fx-font-family:Impact;" +
                        "-fx-font-size: 50;" +
                        "-fx-text-fill: Grey;"
        );

        Label ETHLabelPercent = new Label();
        ETHLabelPercent.setStyle(
                "-fx-aligment:center;" +
                        "-fx-text-aligment: right;" +
                        "-fx-font-family:Impact;" +
                        "-fx-font-size: 50;" +
                        "-fx-text-fill: Grey;"
        );

        Timeline updateLabels = new Timeline(
                new KeyFrame(Duration.seconds(1), actionEvent -> {

                    if (ping()){
                        setBTC(BTCLabelPrice, BTCLabelPercent, bitcoinClass.getBTCPrice(), bitcoinClass.getBTCPercent());
                        setETH(ETHLabelPrice, ETHLabelPercent, ethereumClass.getETHPrice(), ethereumClass.getETHPercent());
                    } else {
                        setBTC(BTCLabelPrice, BTCLabelPercent, "Brak danych", "Brak danych");
                        setETH(ETHLabelPrice, ETHLabelPercent, "Brak danych", "Brak danych");
                    }


                })
        );
        updateLabels.setCycleCount(Timeline.INDEFINITE);
        updateLabels.play();


        ETHinfoVBox.getChildren().addAll(ETHLabelPrice, ETHLabelPercent);

        ETHHbox.getChildren().addAll(ETHNameLabel, ETHinfoVBox);

        VBox mainVBox = new VBox(BTCHbox, ETHHbox);

        mainVBox.setBackground(new Background(new BackgroundFill(Paint.valueOf("Black"), CornerRadii.EMPTY, Insets.EMPTY)));

        Scene scene = new Scene(mainVBox);

        mainStage.setWidth(310);
        mainStage.setHeight(250);
//        mainStage.setY(-260);
//        mainStage.setX(-300);
        mainStage.setY(0);
        mainStage.setX(0);
        mainStage.setResizable(false);

        mainStage.setScene(scene);
        mainStage.show();

        mainStage.setOnCloseRequest((event) -> System.exit(0));

    }

    private void setBTC(Label BTCLabelPrice, Label BTCLabelPercent, String BTCPrice, String BTCPercent){

        BTCLabelPrice.setText(BTCPrice);
        BTCLabelPercent.setText(BTCPercent + " %");
        if (BTCLabelPercent.getText().charAt(0) != '-')    {
            BTCLabelPercent.setTextFill(Paint.valueOf("Green"));
        } else {
            BTCLabelPercent.setTextFill(Paint.valueOf("Red"));
        }

    }

    private void setETH(Label BTCLabelPrice, Label BTCLabelPercent, String ETHPrice, String ETHPercent){

        BTCLabelPrice.setText(ETHPrice);
        BTCLabelPercent.setText(ETHPercent + " %");
        if (BTCLabelPercent.getText().charAt(0) != '-')    {
            BTCLabelPercent.setTextFill(Paint.valueOf("Green"));
        } else {
            BTCLabelPercent.setTextFill(Paint.valueOf("Red"));
        }


    }


    private static boolean ping() {
        try {

            URL url = new URL("https://www.binance.com/pl");

            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

            connection.setRequestMethod("HEAD");


//            System.out.println("" + connection.getResponseCode());


        } catch (IOException e) {

            return false;

        }
        return true;
    }
}

