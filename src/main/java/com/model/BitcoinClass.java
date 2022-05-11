package com.model;

import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.market.Candlestick;

public class BitcoinClass{

    BinanceApiRestClient client;


    private String BTCPrice;
    private String BTCPercent;
    private Candlestick BTCDaily;


    public BitcoinClass(BinanceApiRestClient client, Candlestick BTCDaily){
        this.client = client;
        this.BTCDaily = BTCDaily;
    }


    public void setBTCPrice() {

            String tempPrice = client.getPrice("BTCUSDT").getPrice();
            String finalPrice;
            for (int i = 0; i < tempPrice.length(); i++) {
                finalPrice = String.valueOf(tempPrice.charAt(i));
                if (finalPrice.contains(".")) {
                    BTCPrice = tempPrice.substring(0, i + 3);
                    setBTCPercent(BTCDaily);
                    break;
                }
            }
    }

    public void setBTCPercent(Candlestick BTCDaily) {
        String percent = null;
        double open = Double.parseDouble(BTCDaily.getOpen().trim());
        double currentPrice = Double.parseDouble(client.get24HrPriceStatistics("BTCUSDT").getLastPrice().trim());

        if (currentPrice != open) {
            String tempPrice = (currentPrice - open) / open * 100 + " %";
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
        BTCPercent = percent;

    }



    public String getBTCPrice(){

        return BTCPrice;

    }
    public String getBTCPercent(){
        return BTCPercent;
    }
    public void setBTCDaily(Candlestick BTCDaily){

        this.BTCDaily = BTCDaily;

    }

}
