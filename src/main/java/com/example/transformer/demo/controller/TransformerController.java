package com.example.transformer.demo.controller;

import com.example.transformer.demo.service.TransformerService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class TransformerController {

    private final TransformerService transformerService;

    public TransformerController(TransformerService transformerService) {
        this.transformerService = transformerService;
    }

    @PostMapping(value = "/transform", consumes = "multipart/form-data", produces = "application/json")
    public ResponseEntity<byte[]> transform(@RequestParam MultipartFile file) throws IOException {
        byte[] transformedJsonData = transformerService.transformJsonData(file);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setContentDispositionFormData( "attachment", file.getOriginalFilename());

        return new ResponseEntity<>(transformedJsonData, httpHeaders, HttpStatus.OK);
    }

}