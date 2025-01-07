package com.example.transformer.demo.service;

import com.example.transformer.demo.util.FileUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.schibsted.spt.data.jslt.Expression;
import com.schibsted.spt.data.jslt.Parser;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class TransformerService {

    private final ObjectMapper objectMapper;

    public TransformerService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public byte[] transformJsonData(MultipartFile jsonFile) throws IOException {
        JsonNode jsonNode = getJsonContent(jsonFile);

        Expression jsltExpression = getJsltExpression();

        JsonNode transformedJson = jsltExpression.apply(jsonNode);

        return transformedJson.toString().getBytes();
    }

    private Expression getJsltExpression() throws IOException {
        String jsltFileData = FileUtil.readFileContent("jslt_expression.jslt");
        return Parser.compileString(jsltFileData);
    }

    private JsonNode getJsonContent(MultipartFile jsonFile) throws IOException {
        String jsonFileData = new String(jsonFile.getBytes());
        return objectMapper.readTree(jsonFileData);
    }
}
