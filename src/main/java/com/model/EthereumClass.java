package com.model;

import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.market.Candlestick;

public class EthereumClass{

    BinanceApiRestClient client;
    private String ETHPrice;
    private String ETHPercent;
    private Candlestick ETHDaily;


    public EthereumClass(BinanceApiRestClient client, Candlestick ETHDaily) {

        this.client = client;
        this.ETHDaily = ETHDaily;
    }



    public void setETHPrice() {

        String tempPrice = client.getPrice("ETHUSDT").getPrice();
        String finalPrice;
        for (int i = 0; i < tempPrice.length(); i++) {
            finalPrice = String.valueOf(tempPrice.charAt(i));
            if (finalPrice.contains(".")) {
                ETHPrice = tempPrice.substring(0, i + 3);
                setETHPercent(ETHDaily);
                break;
            }
        }
    }

    public void setETHPercent(Candlestick ETHDaily){
        String percent = null;
        double openPrice = Double.parseDouble(ETHDaily.getOpen().trim());
        double currentPrice = Double.parseDouble(client.get24HrPriceStatistics("ETHUSDT").getLastPrice().trim());

        if (currentPrice != openPrice) {
            String tempPrice = (currentPrice - openPrice) / openPrice * 100 + " %";
            String finalPrice;
            for (int i = 0; i < tempPrice.length(); i++) {
                finalPrice = String.valueOf(tempPrice.charAt(i));
                if (finalPrice.contains(".")) {
                    percent = tempPrice.substring(0, i + 3);
                    break;
                }
            }
        } else {
            percent = "0";
        }
        ETHPercent = percent;
    }

    public String getETHPrice(){
        return ETHPrice;
    }
    public String getETHPercent() {
        return ETHPercent;
    }
    public void setETHDaily(Candlestick ETHDaily){
        this.ETHDaily = ETHDaily;
    }
}
