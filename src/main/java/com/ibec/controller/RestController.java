package com.ibec.controller;

import com.google.gson.Gson;
import com.ibec.entity.City;
import com.ibec.entity.Location;
import com.ibec.service.CityFactory;
import com.ibec.service.LocationFactory;
import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static com.ibec.controller.Constants.DIV_SELECT;
import static com.ibec.controller.Constants.DIV_TEMPLATE;


@WebServlet(
    name = "Weather",
    urlPatterns = {"/rest/*"})
public class RestController extends HttpServlet {
  private Gson gson;
  LocationFactory locationFactory = new LocationFactory();
  Document page;
  CityFactory cityFactory = new CityFactory();
  private static final Logger log = Logger.getLogger(RestController.class);

  @Override
  public void init() {
    gson = new Gson();
  }

  public String getCityWeatherByUrl(String cityReq) throws IOException {
     page = cityFactory.getCityUrlPage(cityReq);

    Element divWeather = page.select(DIV_TEMPLATE).first();
    Element weatherNow = divWeather.select(DIV_SELECT).first();
    return weatherNow.text();
  }

  private static boolean isInteger(String s) throws NumberFormatException {
    try {
      Integer.parseInt(s);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

    log.info("Пришел запрос на URI: " + req.getMethod() + req.getRequestURI());
    String pathInfo = req.getParameter("city");
    req.setCharacterEncoding("UTF-8");

    try {
      resp.setContentType("application/json; charset=UTF-8");

      String weatherFinal = getCityWeatherByUrl(pathInfo);

      City city = new City(pathInfo, weatherFinal);
      log.info("Обработан ответ на запрос погоды в городе: " + city.getName());
      String employeeJsonString = this.gson.toJson(city);

      resp.setStatus(200);
      PrintWriter out = resp.getWriter();
      out.print(employeeJsonString);
      out.flush();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    log.info("Пришел запрос на URL: " + req.getMethod() + req.getPathInfo());
    String pathInfo = req.getPathInfo();
    req.setCharacterEncoding("UTF-8");
    resp.setContentType("text/HTML; charset=UTF-8");

    try {
      String locNumb = pathInfo;
      String utcName = locationFactory.getTimeZoneByRegion(locNumb);
      Location location = new Location(locNumb, utcName);
      log.info("Установлен часовой пояс: " + location.getTimeZone());
      String employeeJsonString = this.gson.toJson(location);
      resp.setStatus(200);
      PrintWriter out = resp.getWriter();
      out.write(employeeJsonString);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
