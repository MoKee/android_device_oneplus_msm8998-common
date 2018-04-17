package com.oem.os;

interface IOnePlusNfcService {

    boolean applyConfig();

    String getDieId();

    List<String> getSupportCardTypes();

    List<String> getSupportNfcConfigs();

    void setCardType(String type);

    void setNfcConfig(String config);

    void setSupportCardTypes(in List<String> cardTypes);

}
