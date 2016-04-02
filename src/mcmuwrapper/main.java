package mcmuwrapper;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;

public class main {
    public static void main(String[] args) {
        try {
            main.saveUrl("mcmu.jar", "http://host.googledrive.com/host/0B2VWlN6I2YDGWnUtamowSjgyXzg/mcmu.jar");
            File file = new File("mcmu.jar");
            URL url = file.toURI().toURL();
            URL[] urls = new URL[]{url};
            URLClassLoader cl = new URLClassLoader(urls);
            Class cls = cl.loadClass("mcmu.MCMU");
            cls.newInstance();
        }
        catch (IOException | ClassNotFoundException | IllegalAccessException | InstantiationException file) {
            // empty catch block
        }
    }

    public static void saveUrl(String filename, String urlString) throws IOException {
        BufferedInputStream in = null;
        FileOutputStream fout = null;
        try {
            int count;
            in = new BufferedInputStream(new URL(urlString).openStream());
            fout = new FileOutputStream(filename);
            byte[] data = new byte[1024];
            while ((count = in.read(data, 0, 1024)) != -1) {
                fout.write(data, 0, count);
            }
        }
        finally {
            if (in != null) {
                in.close();
            }
            if (fout != null) {
                fout.close();
            }
        }
    }
}