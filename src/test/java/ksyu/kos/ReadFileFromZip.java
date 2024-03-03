package ksyu.kos;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ReadFileFromZip {
    private final ClassLoader cl = ReadFileFromZip.class.getClassLoader();

    public ZipEntry streamZips(String fileName, String zipFilePath) throws Exception {
        try (InputStream getZip = cl.getResourceAsStream(zipFilePath); ZipInputStream zipStream = new ZipInputStream(getZip)) {
            ZipEntry file = zipStream.getNextEntry();
            int n = 0;

            while (file != null) {
                if (file.getName().equals(fileName)) {
                     return file;
                }
                else {throw new IOException("Файла нет в архиве");}
            }

        }
        return null;
    }

    public static void main(String[] args) throws Exception {
        ReadFileFromZip art = new ReadFileFromZip();
        art.streamZips("test.csv","testdata/archive.zip");
        //System.out.println;
    }

}




