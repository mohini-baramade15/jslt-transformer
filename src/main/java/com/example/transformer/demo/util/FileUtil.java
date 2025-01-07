package com.example.transformer.demo.util;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FileUtil {
    public static String readFileContent(String filePath) throws IOException {
        Resource resource = new ClassPathResource(filePath);
        File file = resource.getFile();
        return Files.readString(file.toPath());
    }
}
