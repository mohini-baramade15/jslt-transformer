package com.example.transformer.demo.service;

import com.example.transformer.demo.util.FileUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.schibsted.spt.data.jslt.Expression;
import com.schibsted.spt.data.jslt.Parser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransformerServiceTest {

    @Mock
    private ObjectMapper objectMapper;
    private TransformerService transformerService;

    @BeforeEach
    void setUp() {
        transformerService = new TransformerService(objectMapper);
    }

    @Test
    void shouldTransformJsonData() throws IOException {
        MultipartFile mockFile = mock(MultipartFile.class);
        String jsonContent = "{\"name\": \"Alice\"}";
        when(mockFile.getBytes()).thenReturn(jsonContent.getBytes());

        JsonNode mockJsonNode = mock(JsonNode.class);
        when(objectMapper.readTree(jsonContent)).thenReturn(mockJsonNode);

        String jsltExpressionContent = "{\"UserName\": .name}";
        MockedStatic<FileUtil> mockedFileUtil = Mockito.mockStatic(FileUtil.class);
        mockedFileUtil.when(() -> FileUtil.readFileContent("jslt_expression.jslt"))
                .thenReturn(jsltExpressionContent);

        Expression mockExpression = mock(Expression.class);

        MockedStatic<Parser> mockedParser = Mockito.mockStatic(Parser.class);
        mockedParser.when(() -> Parser.compileString(jsltExpressionContent))
                .thenReturn(mockExpression);

        JsonNode transformedJsonNode = mock(JsonNode.class);
        when(mockExpression.apply(mockJsonNode)).thenReturn(transformedJsonNode);

        String transformedJsonContent = "{\"UserName\": \"Alice\"}";
        when(transformedJsonNode.toString()).thenReturn(transformedJsonContent);

        byte[] result = transformerService.transformJsonData(mockFile);

        assertArrayEquals(transformedJsonContent.getBytes(), result);

        verify(mockFile).getBytes();
        verify(objectMapper).readTree(jsonContent);
        mockedFileUtil.verify(() -> FileUtil.readFileContent("jslt_expression.jslt"));
        verify(mockExpression).apply(mockJsonNode);
        mockedFileUtil.close();
    }

}
