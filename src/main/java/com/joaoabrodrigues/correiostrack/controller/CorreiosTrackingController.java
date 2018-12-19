package com.joaoabrodrigues.correiostrack.controller;

import com.joaoabrodrigues.correiostrack.service.CorreiosTrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/v1/track")
public class CorreiosTrackingController {

    private final CorreiosTrackingService trackingService;

    @Autowired
    public CorreiosTrackingController(CorreiosTrackingService trackingService) {
        this.trackingService = trackingService;
    }

    @CrossOrigin
    @GetMapping(path = "/{object}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> trackObject(@PathVariable("object") String object) {
        return ResponseEntity.ok(trackingService.trackObject(object));
    }
}
