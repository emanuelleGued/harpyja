package com.project.harpyja.model.nymphicus;

// Device.java
public class Device {
    private double batteryLevel;
    private String brand;
    private String currentNetwork;
    private String device;
    private String installID;
    private String language;
    private String manufacturer;
    private String model;
    private String osVersion;
    private String platform;
    private String screenResolution;
    private int sdkVersion;
    private String totalRAM;
    private String totalStorage;

    public Device() {}

    public Device(double batteryLevel, String brand, String currentNetwork, String device,
                  String installID, String language, String manufacturer, String model,
                  String osVersion, String platform, String screenResolution,
                  int sdkVersion, String totalRAM, String totalStorage) {
        this.batteryLevel = batteryLevel;
        this.brand = brand;
        this.currentNetwork = currentNetwork;
        this.device = device;
        this.installID = installID;
        this.language = language;
        this.manufacturer = manufacturer;
        this.model = model;
        this.osVersion = osVersion;
        this.platform = platform;
        this.screenResolution = screenResolution;
        this.sdkVersion = sdkVersion;
        this.totalRAM = totalRAM;
        this.totalStorage = totalStorage;
    }

    public double getBatteryLevel() {
        return batteryLevel;
    }
    public void setBatteryLevel(double batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    public String getBrand() {
        return brand;
    }
    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCurrentNetwork() {
        return currentNetwork;
    }
    public void setCurrentNetwork(String currentNetwork) {
        this.currentNetwork = currentNetwork;
    }

    public String getDevice() {
        return device;
    }
    public void setDevice(String device) {
        this.device = device;
    }

    public String getInstallID() {
        return installID;
    }
    public void setInstallID(String installID) {
        this.installID = installID;
    }

    public String getLanguage() {
        return language;
    }
    public void setLanguage(String language) {
        this.language = language;
    }

    public String getManufacturer() {
        return manufacturer;
    }
    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }
    public void setModel(String model) {
        this.model = model;
    }

    public String getOsVersion() {
        return osVersion;
    }
    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getPlatform() {
        return platform;
    }
    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getScreenResolution() {
        return screenResolution;
    }
    public void setScreenResolution(String screenResolution) {
        this.screenResolution = screenResolution;
    }

    public int getSdkVersion() {
        return sdkVersion;
    }
    public void setSdkVersion(int sdkVersion) {
        this.sdkVersion = sdkVersion;
    }

    public String getTotalRAM() {
        return totalRAM;
    }
    public void setTotalRAM(String totalRAM) {
        this.totalRAM = totalRAM;
    }

    public String getTotalStorage() {
        return totalStorage;
    }
    public void setTotalStorage(String totalStorage) {
        this.totalStorage = totalStorage;
    }
}
