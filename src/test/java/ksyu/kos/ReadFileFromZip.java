package ksyu.kos;

import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ReadFileFromZip {
    private final ClassLoader cl = ReadFileFromZip.class.getClassLoader();
    private String path;

    public ReadFileFromZip(String string) {
        this.path = string;
    }

    public void streamZip () throws Exception {
        try (InputStream getZip = cl.getResourceAsStream(path);
             ZipInputStream zipStream = new ZipInputStream(getZip)) {
            ZipEntry file=zipStream.getNextEntry();
                System.out.println(file.getName());
        }
    }



}
