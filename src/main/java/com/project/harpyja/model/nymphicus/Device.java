package com.project.harpyja.model.nymphicus;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
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

    public Device() {
    }

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

}
