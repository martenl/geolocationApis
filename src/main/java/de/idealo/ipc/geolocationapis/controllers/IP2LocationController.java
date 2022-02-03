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

@Controller
@RequestMapping("/ip2location")
public class IP2LocationController {

    final static String apiKey = "z5DOlwKq8T4b5fSf4hFunnzgChLOkbLyrMLlSr4QXu6vthNdRGiaV7YBU38o0UOj";
    final String apiURL = "https://api.ip2location.com/v2/?ip={ip}&lang=de-de&key={api_key}&format=json&package=WS9&&addon=country,region,city,zip_code";
    final RestTemplate restTemplate = new RestTemplate();

    private Optional<IP2LocationController.IP2LocationResult> getData(String ip) {
        ResponseEntity<IP2LocationController.IP2LocationResult> result = restTemplate.getForEntity(apiURL, IP2LocationController.IP2LocationResult.class, Map.of("ip", ip, "api_Key", apiKey));
        return Optional.ofNullable(result.getBody());
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static private class IP2LocationResult {
        String city_name;
        String zip_code;
        String latitude;
        String longitude;
    }

    @GetMapping
    ModelAndView getIP2LocationData(HttpServletRequest request) {
        long time = System.currentTimeMillis();
        String ip = request.getRemoteAddr();
        Optional<IP2LocationResult> data = getData(ip);
        Long timeItTook = System.currentTimeMillis() - time;
        Map<String, Object> result = data.isPresent() ?
                Map.of( "zipcode", data.get().zip_code, "city", data.get().city_name, "longitude", data.get().longitude, "latitude", data.get().latitude )
                : Map.of();
        Map<String, Object> model = Map.of("ip", ip, "result", result, "time", timeItTook);
        return new ModelAndView("ip2location", model);
    }
}
