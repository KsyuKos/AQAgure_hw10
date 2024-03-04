package ksyu.kos;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ReadFileFromZip {
    private final ClassLoader cl = ReadFileFromZip.class.getClassLoader();

    public ZipInputStream streamZips(String fileName, String zipFilePath) throws Exception {
        InputStream getZip = cl.getResourceAsStream(zipFilePath);
        ZipInputStream zipStream = new ZipInputStream(getZip);
        ZipEntry file = zipStream.getNextEntry();
        if (file != null && file.getName().equals(fileName)) {
            return zipStream;
        } else {
            throw new IOException("Файла нет в архиве");
        }

    }


}






