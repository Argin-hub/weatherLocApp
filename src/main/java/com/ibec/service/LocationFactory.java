package com.ibec.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.ibec.controller.Constants.UTC_5;
import static com.ibec.controller.Constants.UTC_6;

public class LocationFactory {

  private Map<String, String> refOnLocation;

  public void init() {
    refOnLocation = new HashMap<>();
    refOnLocation.put("/1", UTC_5);
    refOnLocation.put("/2", UTC_5);
    refOnLocation.put("/6", UTC_6);
    refOnLocation.put("/7", UTC_6);
  }

  public String getTimeZoneByRegion(String region) throws IOException {
    if (refOnLocation == null) {
      init();
    }

    String timeZone = refOnLocation.get(region);
    return timeZone;
  }
}
