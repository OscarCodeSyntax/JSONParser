import org.junit.jupiter.api.Test;
import org.syntax.parser.Methods;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class ParserTest {


    Methods methods = new Methods();

    @Test
    void testValidJSON() throws IOException {
        String json =Files.readString(Paths.get("src/test/resources/step1/valid.json"));

        Integer actual = methods.isValidJson(json);
        assertEquals(1, actual);

    }

    @Test
    void testInValidJSON_Empty() throws IOException {
        String json =Files.readString(Paths.get("src/test/resources/step1/invalid.json"));

        Integer actual = methods.isValidJson(json);
        assertEquals(0, actual);

    }


    @Test
    void testInValidJSON_InvalidCommaPlacement() throws IOException {
        String json =Files.readString(Paths.get("src/test/resources/step2/invalid.json"));

        Integer actual = methods.isValidJson(json);
        assertEquals(7, actual);

    }

    @Test
    void testInValidJSON_InvalidSpeechMarks() throws IOException {
        String json =Files.readString(Paths.get("src/test/resources/step2/invalid2.json"));

        Integer actual = methods.isValidJson(json);
        assertEquals(7, actual);

    }

    @Test
    void testInValidJSON_NoClosingCurlyBraces() throws IOException {
        String json =Files.readString(Paths.get("src/test/resources/step2/invalid3.json"));

        Integer actual = methods.isValidJson(json);
        assertEquals(2, actual);

    }

    @Test
    void testValidJSON_TwoColon() throws IOException {
        String json =Files.readString(Paths.get("src/test/resources/step2/invalid4.json"));

        Integer actual = methods.isValidJson(json);
        assertEquals(8, actual);

    }

    @Test
    void testValidJSON_TwoObjects() throws IOException {
        String json =Files.readString(Paths.get("src/test/resources/step2/valid.json"));

        Integer actual = methods.isValidJson(json);
        assertEquals(1, actual);

    }

    @Test
    void testValidJSON_NullIntBoolean() throws IOException {
        String json =Files.readString(Paths.get("src/test/resources/step3/valid.json"));

        Integer actual = methods.isValidJson(json);
        assertEquals(1, actual);
    }

    @Test
    void testInValidJSON_InvalidStartToNonStringKeyPairValue() throws IOException {
        String json =Files.readString(Paths.get("src/test/resources/step3/invalid.json"));

        Integer actual = methods.isValidJson(json);
        assertEquals(8, actual);
    }

    @Test
    void testInValidJSON_InvalidExpectedFalseValue() throws IOException {
        String json =Files.readString(Paths.get("src/test/resources/step3/invalid2.json"));

        Integer actual = methods.isValidJson(json);
        assertEquals(10, actual);
    }

    @Test
    void testInValidJSON_InvalidExpectedTrueValue() throws IOException {
        String json =Files.readString(Paths.get("src/test/resources/step3/invalid3.json"));

        Integer actual = methods.isValidJson(json);
        assertEquals(9, actual);
    }

    @Test
    void testInValidJSON_InvalidExpectedNullValue() throws IOException {
        String json =Files.readString(Paths.get("src/test/resources/step3/invalid5.json"));

        Integer actual = methods.isValidJson(json);
        assertEquals(11, actual);
    }



    @Test
    void testInValidJSON_InvalidExpectedString_extraLetterOnEnd() throws IOException {
        String json =Files.readString(Paths.get("src/test/resources/step3/invalid4.json"));

        Integer actual = methods.isValidJson(json);
        assertEquals(13, actual);
    }


    @Test
    void testValidJSON_validNumberEmptyObjectAndArray() throws IOException {
        String json =Files.readString(Paths.get("src/test/resources/step4/valid.json"));

        Integer actual = methods.isValidJson(json);
        assertEquals(1, actual);
    }

    @Test
    void testValidJSON_validNumberPopulatedObjectAndArray() throws IOException {
        String json =Files.readString(Paths.get("src/test/resources/step4/valid2.json"));

        Integer actual = methods.isValidJson(json);
        assertEquals(1, actual);
    }

    @Test
    void testInValidJSON_validNumberPopulatedObjectAndInvalidArray() throws IOException {
        String json =Files.readString(Paths.get("src/test/resources/step4/invalid.json"));

        Integer actual = methods.isValidJson(json);
        assertEquals(17, actual);
    }

    @Test
    void testValidJSON_validNumberPopulatedObjectAndArrayWithSingleQuoteNames() throws IOException {
        String json =Files.readString(Paths.get("src/test/resources/step4/valid3.json"));

        Integer actual = methods.isValidJson(json);
        assertEquals(1, actual);
    }

    @Test
    void testValidJSON_FinalStage1() throws IOException {
        String json =Files.readString(Paths.get("src/test/resources/step5/valid1.json"));

        Integer actual = methods.isValidJson(json);
        assertEquals(1, actual);
    }

    @Test
    void testValidJSON_FinalStage2() throws IOException {
        String json =Files.readString(Paths.get("src/test/resources/step5/valid2.json"));

        Integer actual = methods.isValidJson(json);
        assertEquals(1, actual);
    }

    @Test
    void testInValidJSON_FinalStage1() throws IOException {
        String json =Files.readString(Paths.get("src/test/resources/step5/invalid1.json"));

        Integer actual = methods.isValidJson(json);
        assertEquals(5, actual);
    }

}
