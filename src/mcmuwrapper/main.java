package mcmuwrapper;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class main {
    public static void main(String[] args) {
        try {
            String inf = main.getURL("https://api.github.com/repos/TeamEndAllReality/MCMU/releases/latest");
            Pattern pat = Pattern.compile("\"browser_download_url\":\"(.*?)\"", Pattern.CASE_INSENSITIVE);
            Matcher m = pat.matcher(inf);
            m.find();
            String dlurl = m.group(1);
            System.out.print(dlurl);
            main.saveUrl("mcmu.jar", dlurl);
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
    public static String getURL(String getURL) throws Exception {
        StringBuilder result = new StringBuilder();
        URL url = new URL(getURL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line+"\n");
        }
        rd.close();
        return result.toString();
    }
    public static String getVer() throws Exception {
        StringBuilder result = new StringBuilder();
        URL url = new URL("https://raw.githubusercontent.com/TeamEndAllReality/MCMU/master/ver");
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