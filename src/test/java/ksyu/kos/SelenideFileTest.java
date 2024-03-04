package ksyu.kos;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.*;

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

    @Test
    @DisplayName("Проверка CSV файла")
    void checkCsvFileFromZip() throws Exception {
        try (InputStream getZip = cl.getResourceAsStream("testdata/archive.zip");
             ZipInputStream zipStream = new ZipInputStream(getZip);
             CSVReader csvReader = new CSVReader(new InputStreamReader(zipStream))) {

            ZipEntry file;
            int n = 0;

            while ((file = zipStream.getNextEntry()) != null) {

                if (file.getName().equals("test.csv")) {
                    List<String[]> contentCsv = csvReader.readAll();
                    assertThat(contentCsv.get(0)).isEqualTo(
                            new String[]{"title", "artist", "album", "isrc", "isFound"}
                    );
                    n++;
                }
            }
            if (n == 0){
                throw new IOException("Файла нет в архиве");
            }
        }
    }

    @Test
    @DisplayName("Проверка PDF файла")
    void checkPdfFileFromZip() throws Exception {
        try (InputStream getZip = cl.getResourceAsStream("testdata/archive.zip");
             ZipInputStream zipStream = new ZipInputStream(getZip)) {

            ZipEntry filePdf;
            int n = 0;


            while ((filePdf = zipStream.getNextEntry()) != null) {

                if (filePdf.getName().equals("pdf.pdf")) {
                    PDF contentPdf = new PDF(zipStream);
                    assertThat(contentPdf.numberOfPages).isEqualTo(4);
                    n++;
                }
            }

            if (n == 0){
                throw new IOException("Файла нет в архиве");
            }
        }
    }

    @Test
    @DisplayName("Проверка XSLX файла")
    void checkXlsxFileFromZip() throws Exception {
        try (InputStream getZip = cl.getResourceAsStream("testdata/archive.zip");
             ZipInputStream zipStream = new ZipInputStream(getZip)) {

            ZipEntry fileXlsx;
            int n = 0;

            while ((fileXlsx = zipStream.getNextEntry()) != null) {

                if (fileXlsx.getName().equals("xlsx.xlsx")) {
                    XLS contentXlsx = new XLS(zipStream);
                    assertThat(contentXlsx.
                                    excel.
                                    getSheet("List").
                                    getRow(1).
                                    getCell(1).
                                    getStringCellValue()).isEqualTo("test2");
                    n++;
                }
            }

            if (n == 0){
                throw new IOException("Файла нет в архиве");
            }
        }
    }


    @Test
    @DisplayName("Проверка JSON файла")
    void checkJsonTest() throws Exception {
        InputStream getJson = cl.getResourceAsStream("testdata/json.json");

        ObjectMapper objectMapper = new ObjectMapper();
        Account objectAccount = objectMapper.readValue(getJson, Account.class);
        assertThat(objectAccount.getStatus()).isEqualTo("ACTIVE");
        assertThat(objectAccount.getTransactions().getAmount()).isEqualTo(3000);
        assertThat(objectAccount.getRemote_accounts().get(0).getBalance()).isEqualTo(1000);
        assertThat(objectAccount
                .getRemote_accounts()
                .stream()
                .map(RemoteAccounts::getBalance)
                .toArray()).isEqualTo(new Integer[]{1000, 2000});
    }

}


