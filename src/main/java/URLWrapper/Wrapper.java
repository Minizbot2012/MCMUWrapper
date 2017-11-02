package URLWrapper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Wrapper {
    public static void main(String[] args) {
        try {
            String inf = Wrapper.getURL("https://api.github.com/repos/TeamEndAllReality/MCMU/releases/latest");
            Pattern pat = Pattern.compile("\"browser_download_url\":\"(.*?)\"", Pattern.CASE_INSENSITIVE);
            Matcher m = pat.matcher(inf);
            m.find();
            String dlurl = m.group(1);
            URL jarURL = new URL(dlurl);
            JarURLConnection conn = (JarURLConnection) (new URL(jarURL, "jar:"+dlurl+"!/").openConnection());
            Manifest mf = conn.getManifest();
            ClassLoader cl = URLClassLoader.newInstance(new URL[] {jarURL}, Wrapper.class.getClassLoader());
            Attributes attr = mf.getMainAttributes();
            Class cls = Class.forName(attr.getValue(Attributes.Name.MAIN_CLASS), true, cl);
            cls.newInstance();
        }
        catch (Exception file) {
            // empty catch block
        }
    }
    public Wrapper(String[] args) {
        String jsonURL = args[0];
        try {
            String d = Wrapper.getURL(jsonURL);
            Gson gson = new Gson();
            Type lt = new TypeToken<ArrayList<String>>(){}.getClass();
            ArrayList<String> urls = gson.fromJson(d, lt);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void bootstrapJar(URL jfs[]) throws NoSuchMethodException {
        URLClassLoader classLoader = URLClassLoader.newInstance(null);
        Method m = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
        for (URL jf : jfs) {
            try {
                m.invoke(classLoader, jf);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
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
            result.append(line + "\n");
        }
        rd.close();
        return result.toString();
    }
}