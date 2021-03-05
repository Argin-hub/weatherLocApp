package com.ibec.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static com.ibec.controller.Constants.*;

public class CityFactory {

  private Map<String, String> refOnCityWeather;

  public void init() {
    refOnCityWeather = new HashMap<>();
    refOnCityWeather.put("krg", KRG_TEMPLATE);
    refOnCityWeather.put("almaty", ALMATY_TEMPLATE);
    refOnCityWeather.put("astana", ASTANA_TEMPLATE);
    refOnCityWeather.put("pavlodar", PAVLODAR_TEMPLATE);
  }

  public Document getCityUrlPage(String cityName) throws IOException {
    if (refOnCityWeather == null) {
      init();
    }
    String url = refOnCityWeather.get(cityName);
    Document page = Jsoup.parse(new URL(url), 9000);
    return page;
  }
}
