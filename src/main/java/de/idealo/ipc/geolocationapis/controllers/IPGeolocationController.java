package de.idealo.ipc.geolocationapis.controllers;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import io.ipgeolocation.api.Geolocation;
import io.ipgeolocation.api.GeolocationParams;
import io.ipgeolocation.api.IPGeolocationAPI;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/ipgeolocation")
@Slf4j
public class IPGeolocationController {

    IPGeolocationAPI api = new IPGeolocationAPI("25a6c9417d584f838839e3c582567f64");

    @GetMapping
    ModelAndView ipgeoloactaion(HttpServletRequest request) {
        final String ip = request.getRemoteAddr();
        final GeolocationParams geoParams = new GeolocationParams();
        geoParams.setIPAddress(ip);
        geoParams.setLang("de");
        geoParams.setFields("zipcode,longitude,latitude");
        Geolocation geolocation = api.getGeolocation(geoParams);
        Map<String, String> result = geolocation.getMessage() == null ?
          Map.of("zipcode", geolocation.getZipCode(),"longitude", geolocation.getLongitude(),"latitude", geolocation.getLatitude()) :
          Map.of();
        log.info("msg: {}", geolocation.getMessage());
        log.info("zipcode: {}, longitude: {}, latitude: {}", geolocation.getZipCode(),geolocation.getLongitude(),geolocation.getLatitude());
        Map<String, Object> model = Map.of(
                "ip", ip,
                "result", result,
                "error", geolocation.getMessage() == null ? "" : geolocation.getMessage()
        );
        return new ModelAndView("ipgeolocation", model);
    }
}
