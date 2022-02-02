package de.idealo.ipc.geolocationapis.controllers;

import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/abstract")
public class AbstractAPIController {

    final static String ip = "145.243.167.0";
    final String apiKey = "c1b4c101727c438b8387836d023543ff";
    final String apiURL = "https://ipgeolocation.abstractapi.com/v1/?api_key={api_key}&ip_address={ip_address}";
    final RestTemplate restTemplate = new RestTemplate();

    private Map<String, Object> getData(String ip) {
        long time = System.currentTimeMillis();
        ResponseEntity<AbstractAPIStackResult> result = restTemplate.getForEntity(apiURL, AbstractAPIStackResult.class, Map.of("api_key", apiKey, "ip_address", ip));
        Long timeItTook = System.currentTimeMillis() - time;
        if(result.hasBody()) {
            AbstractAPIStackResult data = result.getBody();;
            return Map.of(
                    "ip", ip,
                    "time", timeItTook,
                    "zipcode", data.postal_code,
                    "city", data.city,
                    "longitude", data.longitude,
                    "latitude", data.latitude
            );
        }
        return Map.of();

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static private class AbstractAPIStackResult {
        String city;
        String postal_code;
        String latitude;
        String longitude;
    }

    @GetMapping
    ModelAndView getGeoIPFromAbstract(HttpServletRequest request) {
        Map<String, Object> model = Map.of("result", getData(request.getRemoteAddr()));
        return new ModelAndView("abstract", model);
    }
}
