package com.example.parkingLot.controller;

import com.example.parkingLot.service.DisplayBoardService;
import lombok.RequiredArgsConstructor;
import org.slf4j.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/display")
@RequiredArgsConstructor
public class DisplayBoardController {
    private static final Logger log = LoggerFactory.getLogger(DisplayBoardController.class);
    private final DisplayBoardService displayBoardService;

    @GetMapping("/summary")
    public ResponseEntity<Map<String, Object>> getDisplaySummary(){
        return ResponseEntity.ok(displayBoardService.getDisplaySummary());
    }
}
