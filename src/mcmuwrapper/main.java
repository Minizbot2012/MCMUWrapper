package mcmuwrapper;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;

public class main {
    public static void main(String[] args) {
        try {
            String ver = getVer();
            main.saveUrl("mcmu.jar", String.format("https://github.com/end-all-reality/MCMU/releases/download/%s/MCMU.jar", ver));
            File file = new File("mcmu.jar");
            URL url = file.toURI().toURL();
            URL[] urls = new URL[]{url};
            URLClassLoader cl = new URLClassLoader(urls);
            Class cls = cl.loadClass("mcmu.MCMU");
            cls.newInstance();
        }
        catch (Exception file) {
            // empty catch block
        }
    }

    public static String getVer() throws Exception {
        StringBuilder result = new StringBuilder();
        URL url = new URL("https://raw.githubusercontent.com/end-all-reality/MCMU/master/ver");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        rd.close();
        return result.toString();
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