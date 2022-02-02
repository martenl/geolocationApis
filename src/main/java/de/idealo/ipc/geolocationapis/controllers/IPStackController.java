package de.idealo.ipc.geolocationapis.controllers;

import java.util.Map;
import java.util.Optional;

import org.springframework.http.RequestEntity;
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
@RequestMapping("/ipstack")
public class IPStackController {

    final static String ip = "145.243.167.0";
    final static String apiKey = "302237f69426cd8bd43813cfd601ddc9";
    final String apiURL = "http://api.ipstack.com/{ip}?access_key={apiKey}";
    final RestTemplate restTemplate = new RestTemplate();

    private Optional<IPStackResult> getData() {
        ResponseEntity<IPStackResult> result = restTemplate.getForEntity(apiURL, IPStackResult.class, Map.of("ip", ip, "apiKey", apiKey));
        return Optional.ofNullable(result.getBody());
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static private class IPStackResult {
        String city;
        String zip;
        String latitude;
        String longitude;
    }

    @GetMapping
    ModelAndView getIPStack() {
        long time = System.currentTimeMillis();
        Optional<IPStackResult> result = getData();
        Long timeItTook = System.currentTimeMillis() - time;
        Map<String, String> data = result.isPresent() ?
                Map.of( "zipcode", result.get().zip, "city", result.get().city, "longitude", result.get().longitude, "latitude", result.get().latitude ) :
                Map.of();
        Map<String, Object> model = Map.of("ip", ip,"result", data, "time", timeItTook);
        return new ModelAndView("ipstack", model);
    }
}
