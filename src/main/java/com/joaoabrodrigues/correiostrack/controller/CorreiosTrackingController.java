package com.joaoabrodrigues.correiostrack.controller;

import com.joaoabrodrigues.correiostrack.service.CorreiosTrackingService;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/track")
public class CorreiosTrackingController {

    private static final int PRETTY_PRINT_INDENT_FACTOR = 4;

    private final CorreiosTrackingService trackingService;

    @Autowired
    public CorreiosTrackingController(CorreiosTrackingService trackingService) {
        this.trackingService = trackingService;
    }

    @GetMapping(path = "/{object}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> trackObject(@PathVariable("object") String object) {
        try {
            JSONObject json = XML.toJSONObject(trackingService.trackObject(object));
            String prettyJson = json.toString(PRETTY_PRINT_INDENT_FACTOR);

            return ResponseEntity.ok(prettyJson);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(String.format("Didn't work :( \n Error: %s ", e.getMessage()));
        }
    }
}
