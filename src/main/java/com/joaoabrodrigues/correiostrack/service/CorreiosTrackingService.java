package com.joaoabrodrigues.correiostrack.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;

@Service
public class CorreiosTrackingService {

    @Value("${correios.url.service}")
    private String url;

        public String trackObject(String object) {
            String request = "<rastroObjeto>"
                    + "<usuario>MobileXect</usuario>"
                    + "<senha>DRW0#9F$@0</senha>"
                    + "<tipo>L</tipo>"
                    + "<resultado>L</resultado>"
                    + "<objetos>"
                    + object
                    + "</objetos>"
                    + "<lingua>101</lingua>"
                    + "</rastroObjeto>";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_XML);

            HttpEntity<String> entity = new HttpEntity<>(request, headers);

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));

            return restTemplate.postForObject(url, entity, String.class);
        }
}