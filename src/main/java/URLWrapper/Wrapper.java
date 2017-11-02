package URLWrapper;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;

public class Wrapper {
    public static void main(String[] args) {
        try {
            new Wrapper(args[0]);
        }
        catch (Exception file) {
            // empty catch block
        }
    }
    public class WrapperObject {
        String mainClass;
        ArrayList<String> urls;
        public URL[] getURLs() {
            final URL[] urlArray = new URL[this.urls.size()];
            int i = 0;
            this.urls.forEach(url-> {
                try {
                    urlArray[i] = new URL("jar", "", url+"!/");
                } catch (MalformedURLException e) {
                    System.out.println("[Error] on URL: "+url);
                }
            });
            return urlArray;
        }

        public String getMainClass() {
            return mainClass;
        }
    }
    public Wrapper(String jsonURL) {
        try {
            String d = Wrapper.getURL(jsonURL);
            Gson gson = new Gson();
            WrapperObject urls = gson.fromJson(d, WrapperObject.class);
            URLClassLoader cl = bootstrapJar(urls.getURLs());
            Class klass = Class.forName(urls.getMainClass(), false, cl);
            klass.newInstance();
            cl.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public URLClassLoader bootstrapJar(URL jfs[]) throws NoSuchMethodException {
        return new URLClassLoader(jfs, Wrapper.class.getClassLoader());
    }


    public static String getURL(String getURL) throws Exception {
        StringBuilder result = new StringBuilder();
        URL url = new URL(getURL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line + "\n");
        }
        rd.close();
        return result.toString();
    }
}