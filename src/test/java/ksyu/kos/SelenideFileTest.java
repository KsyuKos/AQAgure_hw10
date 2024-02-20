package ksyu.kos;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.opencsv.CSVReader;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static java.nio.charset.StandardCharsets.UTF_8;

public class SelenideFileTest {

    @Test
    @Disabled
    void downloadFile() throws Exception {
        open("https://github.com/junit-team/junit5/blob/main/README.md");
        File file = $("a[data_testid='raw-button']")
                .download();

        try (InputStream fileStream = new FileInputStream(file)) {
            byte[] content = fileStream.readAllBytes();
            String contentAsString = new String(content, UTF_8);
            Assertions.assertTrue(
                    contentAsString.contains("Contributions to JUnit 5 are both welcomed and appreciated. For specific guidelines"));
        }

        String contentAsStringSimple = FileUtils
                .readFileToString(file, UTF_8);

    }

    private final ClassLoader cl = SelenideFileTest.class.getClassLoader();
    //private final Gson gson = new Gson();

    @Test
    void checkFileFromZip() throws Exception {
        try (InputStream getZip = cl.getResourceAsStream("testdata/archive.zip");
             ZipInputStream zipStream = new ZipInputStream(getZip);
             CSVReader csvReader = new CSVReader(new InputStreamReader(zipStream))) {

            ZipEntry file;

            while ((file = zipStream.getNextEntry()) != null) {
                //byte[] byteFile = zipStream.readAllBytes();
                //String byteFileToString = new String(byteFile, UTF_8);

                if (file.getName().equals("csv.csv")) {
                    List<String[]> contentCsv = csvReader.readAll();
                    Assertions.assertArrayEquals(
                            new String[]{"title", "artist", "album", "isrc", "isFound"}, contentCsv.get(0)
                    );
                } else if (file.getName().equals("pdf.pdf")) {
                    PDF contentPdf = new PDF(zipStream);
                    Assertions.assertEquals(4, contentPdf.numberOfPages);
                } else if (file.getName().equals("xlsx.xlsx")) {
                    XLS contentXlsx = new XLS(zipStream);
                    Assertions.assertEquals("test2",
                                    contentXlsx.
                                            excel.
                                            getSheet("List").
                                            getRow(1).
                                            getCell(1).
                                            getStringCellValue());
                }

            }
        }
    }

    @Test
    void checkJsonTest () throws Exception {
        InputStream getJson = cl.getResourceAsStream("testdata/json.json");
             //Reader getJsonReader = new InputStreamReader(getJson)) {
            //JsonObject obj = gson.fromJson(getJsonReader, JsonObject.class);
            //Assertions.assertEquals("ACTIVE", obj.get("status").getAsString());
            //Assertions.assertArrayEquals(asList(), obj.get("status").getAsString());

            ObjectMapper objectMapper = new ObjectMapper();
            Account objectAccount = objectMapper.readValue(getJson, Account.class);
            Assertions.assertEquals(objectAccount.getStatus(), "ACTIVE");
            Assertions.assertEquals(objectAccount.getTransactions().getAmount(), 3000);
            Assertions.assertEquals(objectAccount.getRemote_accounts().get(0).getBalance(), 1000);
        }
    }


