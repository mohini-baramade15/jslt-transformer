package com.example.transformer.demo.controller;

import com.example.transformer.demo.service.TransformerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class TransformerControllerTest {

    @Mock
    private TransformerService transformerService;

    @InjectMocks
    private TransformerController transformerController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(transformerController).build();
    }


    @Test
    void shouldTransform() throws Exception {
        String fileName = "testFile.json";
        byte[] fileContent = "{\"id\":\"1\"}".getBytes();
        MockMultipartFile file = new MockMultipartFile("file", fileName, MediaType.APPLICATION_JSON_VALUE, fileContent);

        byte[] transformedData = "{\"userId\":\"1\"}".getBytes();
        when(transformerService.transformJsonData(file)).thenReturn(transformedData);


        mockMvc.perform(multipart("/transform")
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(header().string("Content-Disposition", "form-data; name=\"attachment\"; filename=\"" + fileName + "\""))
                .andExpect(content().bytes(transformedData));
    }
}
