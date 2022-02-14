package ir.co.sadad.paymentBill.commons;

import java.io.*;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class ResourceBundleFactory {

    public ResourceBundle produceBundleWithLocale(Locale locale, String bundleName) {

        if (locale.getLanguage().equals(new Locale("fa").getLanguage())) {
            StringBuilder bundleNameSb = new StringBuilder();
            bundleNameSb
                    .append(bundleName)
                    .append("_")
                    .append("fa")
                    .append(".properties");
            InputStream stream = getClass().getClassLoader().getResourceAsStream(bundleNameSb.toString());
            Reader reader = null;
            ResourceBundle bundle = null;
            try {
                reader = new InputStreamReader(stream, "UTF-8");
                bundle = new PropertyResourceBundle(reader);
                return bundle;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return ResourceBundle.getBundle(bundleName, Locale.ENGLISH);
            } catch (IOException e) {
                e.printStackTrace();
                return ResourceBundle.getBundle(bundleName, Locale.ENGLISH);
            }
        }else {
            return ResourceBundle.getBundle(bundleName, Locale.ENGLISH);
        }
    }
    private String getLocale(Locale locale) {
        if (locale.getLanguage().equals(new Locale("fa").getLanguage()))
            return "fa";
        else if (locale.getLanguage().equals(new Locale("en").getLanguage()))
            return "en";
        else return "en";
    }

}
