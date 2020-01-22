package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;
import java.util.Scanner;
import interfaces.IExternalAPIManagement;

/**
 *
 * @author Henning
 */
public class ExternalAPIManagement implements IExternalAPIManagement {

    @Override
    public <Any> Any readObject(URL url, Class objectClass) {
        final ObjectMapper MAPPER = new ObjectMapper();
        try {
            return (Any) MAPPER.readValue(collectJsonString(url), objectClass);
        } catch (IOException ex) {
            System.out.println("IOException in readObject" + ex);
        }
        return null;
    }

    @Override
    public <Any> Any readObjects(URL url, Class objectClass) {
        final ObjectMapper MAPPER = new ObjectMapper();
        try {
            return MAPPER.readValue(collectJsonString(url), MAPPER
                    .getTypeFactory()
                    .constructCollectionType(List.class, objectClass));
        } catch (IOException ex) {
            System.out.println("IOException in readObjects" + ex);
        }
        return null;
    }

    @Override
    public String collectJsonString(URL url) {
        HttpURLConnection con = null;
        try {
            con = (HttpURLConnection) url.openConnection();
            try {
                con.setRequestMethod("GET");
            } catch (ProtocolException ex) {
                System.out.println("Could not set connection to 'GET'" + "PROTOCOL EXCEPTION");
            }
            con.setRequestProperty("Accept", "application/json;charset=UTF-8");
            con.setRequestProperty("User-Agent", "server");
            StringBuilder jsonStr = new StringBuilder();
            try (Scanner scan = new Scanner(con.getInputStream())) {
                while (scan.hasNext()) {
                    jsonStr.append(scan.nextLine());
                }
            }
            System.out.println("JSON String: " + jsonStr);
            return jsonStr.toString();
        } catch (IOException ex) {
            System.out.println("IO Exception in collectJsonString" + ex);
        } finally {
            con.disconnect();
        }
        return null;
    }

    public enum ExternalURL {
        EXAMPLE01 {
            @Override
            public URL URLConcat(String id) {
                try {
                    return new URL("https://www.google.com/" + id);
                } catch (MalformedURLException ex) {
                    System.out.println("Bad URL in URLConc " + this.name() + "\n" + ex);
                }
                return null;
            }
        },
        EXAMPLE02 {
            @Override
            public URL URLConcat(String id) {
                try {
                    return new URL("https://www.theburpsuite.com/" + id);
                } catch (MalformedURLException ex) {
                    System.out.println("Bad URL in URLConc " + this.name() + "\n" + ex);
                }
                return null;
            }
        },
        //Testing
        JSON_PLACEHOLDER {
            @Override
            public URL URLConcat(String id) {
                try {
                    return new URL("https://jsonplaceholder.typicode.com/" + id);
                } catch (MalformedURLException ex) {
                    System.out.println("Bad URL in URLConc " + this.name() + "\n" + ex);
                }
                return null;
            }
        };

        public abstract URL URLConcat(String id);
    }
}
