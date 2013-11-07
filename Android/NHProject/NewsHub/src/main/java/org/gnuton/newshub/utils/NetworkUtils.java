package org.gnuton.newshub.utils;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by gnuton on 6/8/13.
 */

public class NetworkUtils {
    public static String getDomainName(String url) {
        URI uri = null;
        url = url.replaceFirst("\\?.*","");
        try {
            uri = new URI(url);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }

        String domain = uri.getHost();
        if (domain == null || domain == "")
            return "";
        return domain.startsWith("www.") ? domain.substring(4) : domain;
    }

    public static String getMoreDetailedDomainName(String url) {
        URI uri = null;
        url = url.replaceFirst("\\?.*","");
        try {
            uri = new URI(url);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }

        String domain = uri.getHost() + uri.getPath();
        if (domain == null || domain == "")
            return "";
        return domain.startsWith("www.") ? domain.substring(4) : domain;
    }
}
