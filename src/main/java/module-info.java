module BinanceNakladka {

    requires javafx.base;
    requires javafx.controls;
    requires javafx.graphics;
    requires jackson.annotations;
    requires org.apache.commons.lang3;
    requires com.fasterxml.jackson.databind;
    requires okhttp3;
    requires retrofit2;
    requires retrofit2.converter.jackson;
    requires com.fasterxml.jackson.core;
    requires okio;
    requires org.apache.commons.codec;

    exports com to javafx.graphics, com.fasterxml.jackson.databind;
    exports com.binance.api.client to com.fasterxml.jackson.databind;
    exports com.binance.api.client.domain.market to com.fasterxml.jackson.databind;
    exports com.model to com.fasterxml.jackson.databind, javafx.graphics;

}