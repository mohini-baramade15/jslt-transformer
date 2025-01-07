package com.example.transformer.demo.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class FileUtilTest {

    @Test
    void shouldReadFileContent() throws IOException {
        String expectedOutput = """
                [
                  {
                    "id": 1,
                    "name": "Alice",
                    "email": "alice@example.com",
                    "age": 30,
                    "subscriptionStatus": "active"
                  },
                  {
                    "id": 2,
                    "name": "Bob",
                    "email": "bob@example.com",
                    "age": 25,
                    "subscriptionStatus": "inactive"
                  }
                ]""";

        String result = FileUtil.readFileContent("test_input.json");

        assertEquals(expectedOutput, result);

    }

    @Test
    void shouldThrowExceptionWhenFileNotFound() {
        String invalidFilePath = "input2.json";

        assertThrows(IOException.class, () -> FileUtil.readFileContent(invalidFilePath));
    }

}
