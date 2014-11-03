
package introsde.solution.ehealth;

import introsde.solution.ehealth.dozer.*;
import introsde.solution.ehealth.model.MeasureType;
import introsde.solution.ehealth.model.Person;
import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * This class is the starting point, from which the server runs.
 */
public class App
{
    //on ubuntu to run server on port less of 443 we need to be super user
    private static final URI BASE_URI = URI.create("http://localhost:5900/giulianotortoreto/");
    public static void main(String[] args) throws IllegalArgumentException, IOException, URISyntaxException
    {
    	System.out.println("Starting sdelab standalone HTTP server...");
        JdkHttpServerFactory.createHttpServer(BASE_URI, createApp());
        System.out.println("Server started on " + BASE_URI + "\n[kill the process to exit]");

    }

    public static ResourceConfig createApp() {
    	System.out.println("Starting sdelab REST services...");
        return new MyApplicationConfig();
    }


}
