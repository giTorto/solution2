import java.net.URI;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import model.*;
import org.glassfish.jersey.client.ClientConfig;
import wrapper.WrapperMeasureHistory;
import wrapper.WrapperMeasureTypes;
import wrapper.WrapperPeople;

public class Test {

    private static URI base_URL;
    private static PersonBean first_person_id;
    private static PersonBean last_person_id;
    private static Integer errorsXML = 0;
    private static Integer errorsJSON = 0;
    private static PersonBean addedtoDelete;
    private static List<String> measure_types;
    private static String measureTypeSaved38;
    private static MeasureHistoryBean measureSaved38;
    private static Integer id38;
    private static MeasureHistoryBean measureAdded;
    private static Entity personXMLMod = Entity.xml("<person><firstname>Jacquelin</firstname></person>");
    private static Entity personJSONMod = Entity.json(" { \"firstname\": \"Margaret\" }");
    private static Entity personXMLAdd = Entity
            .xml("<person><firstname>Chuck</firstname><lastname>Norris</lastname><birthdate>1945-01-01</birthdate><healthProfile><weight>78.9</weight><height>172</height></healthProfile></person>");
    private static Entity personJSONAdd = Entity
            .json("{\"firstname\": \"Chuck\", \"lastname\": \"Norris\",\"birthdate\": \"1945-01-01\", \"healthProfile\" : {\"weight\"  : 78.9, \"height\"  : 172}}");
    private static Entity measureXMLAdd = Entity
            .xml("<measure><value>72</value><created>2011-12-09</created></measure>");
    private static Entity measureJSONAdd = Entity
            .json("{\"value\": \"72\", \"created\": \"2011-12-09\"}");
    private static Entity measureXMLPut = Entity
            .xml("<measure> <value>90</value> <created>2011-12-09</created> </measure>");
    private static Entity measureJSONPut = Entity
            .json("{\"value\": \"90\", \"created\": \"2011-12-09\"}");

    public static void main(String[] args) {
        setBaseURI(args[0]);
        String given_date_before = args[2];
        String given_date_after = args[1];
        String given_min = args[3];
        String given_max = args[4];

        ClientConfig clientConfig = new ClientConfig();
        Client client = ClientBuilder.newClient(clientConfig);
        WebTarget service = client.target(getBaseURI());
        String type = MediaType.APPLICATION_XML;
        Integer[] errors = new Integer[2];


        for (int i = 0; i < 2; i++) {

            errors[i] = 0;

            //Step 3.1 // GET /person R#1
            try {
                errors[i] += doRequest1(service, type);
            } catch (NullPointerException e) {
                errors[i]++;
            }
            //Step 3.2 // GET /person/id R#2
            try {
                errors[i] += doRequest2(service, type, first_person_id.getId());
            } catch (NullPointerException e) {
                errors[i]++;
            }
            //Step 3.3 // PUT /person/id R#3
            try {
                errors[i] += doRequest3(service, type);
            } catch (NullPointerException e) {
                errors[i]++;
            }

            try {
                //Step 3.4 // POST /person/
                errors[i] += doRequest4(service, type);
            } catch (NullPointerException e) {
                errors[i]++;
            }
            //Step 3.5 // DELETE /person/id
            try {
                errors[i] += doRequest5(service, type);
            } catch (NullPointerException e) {
                errors[i]++;
            }
            //Step 3.6 // GET /measureTypes
            try {
                errors[i] += doRequest6(service, type);
            } catch (NullPointerException e) {
                errors[i]++;
            }
            //Step 3.7
            try {
                errors[i] += doRequest7(service, type);
            } catch (NullPointerException e) {
                errors[i]++;
            }
            //Step 3.8
            try {
                errors[i] += doRequest8(service, type);
            } catch (NullPointerException e) {
                errors[i]++;
            }
            //Step 3.9
            try {
                errors[i] += doRequest9(service, type);
            } catch (NullPointerException e) {
                errors[i]++;
            }
            //Step 3.10
            try {
                errors[i] += doRequest10(service, type);
            } catch (NullPointerException e) {
                errors[i]++;
            }
            //Step 3.11
            try {
                errors[i] += doRequest11(service, type, given_date_after, given_date_before);
            } catch (NullPointerException e) {
                errors[i]++;
            }
            //Step 3.12
            try {
                errors[i] += doRequest12(service, type, "weight", given_min, given_max);
            } catch (NullPointerException e) {
                errors[i]++;
            }
            type = MediaType.APPLICATION_JSON;
        }

        errorsXML = errors[0];
        errorsJSON = errors[1];

        System.out.println("Errors XML:" + errorsXML);
        System.out.println("Errors JSON:" + errorsJSON);

    }

    private static URI getBaseURI() {
        return Test.base_URL;
    }

        private static void setBaseURI(String base_url) {
        Test.base_URL = UriBuilder.fromUri(base_url).build();
    }

    private static void printOnScreenHeader(Integer number, String method, String url,
                                            String accept, String content, Response response) {

        System.out.println("Request # " + number.toString() + ": " + method +
                " " + url + " Accept: " + accept + " Content-type: " + content);
        System.out.println("=> Result: [RESPONSE STATUS = " + response.getStatusInfo().getReasonPhrase() + "]");
        System.out.println("=> HTTP Status: [HTTP STATUS CODE = " + response.getStatusInfo().getStatusCode() + "]");
        System.out.println("[BODY]");

    }

    private static void printBodyXML(Class givenClass, Object givenObject) {
        JAXBContext jaxbContext = null;
        try {
            jaxbContext = JAXBContext.newInstance(givenClass);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            // output pretty printed
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            jaxbMarshaller.marshal(givenObject, System.out);


        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    private static void printBodyJSON(Object givenObject) {
        // Jackson Object Mapper
        ObjectMapper mapper = new ObjectMapper();

        // Adding the Jackson Module to process JAXB annotations
        JaxbAnnotationModule module = new JaxbAnnotationModule();

        // configure as necessary
        mapper.registerModule(module);

        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        mapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);

        String result = null;
        try {
            result = mapper.writeValueAsString(givenObject);
            System.out.println(result);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private static int doRequest1(WebTarget service, String type) throws NullPointerException {
        Response response = service.path("person").request()
                .accept(type)
                .header("Content-type", type)
                .get();
        WrapperPeople people = response.readEntity(WrapperPeople.class);

        printOnScreenHeader(1, "GET", base_URL + "person",
                type, type, response);

        printBody(type, people);

        first_person_id = people.list.get(0);
        last_person_id = people.list.get(people.list.size() - 1);

        if (people.list.size() <= 2) {
            System.out.println("ERROR");
            return 1;
        } else
            System.out.println("OK");
        return 0;
    }

    private static int doRequest2(WebTarget service, String type, Integer id) throws NullPointerException {
        Response response = service.path("person/" + id)
                .request()
                .accept(type)
                .header("Content-type", type)
                .get();
        PersonBean person = response.readEntity(PersonBean.class);

        printOnScreenHeader(2, "GET", base_URL + "person/" + id,
                type, type, response);

        printBody(type, person);

        if (response.getStatus() == 202 || response.getStatus() == 200) {
            System.out.println("OK");
            return 0;
        } else
            System.out.println("ERROR");
        return 1;
    }

    private static int doRequest3(WebTarget service, String type) throws NullPointerException {
        Response response = null;
        if (type.equals(MediaType.APPLICATION_XML))
            response = service.path("person/" + first_person_id.getId()).request()
                    .accept(type)
                    .header("Content-type", type)
                    .put(personXMLMod);
        else
            response = service.path("person/" + first_person_id.getId()).request()
                    .accept(type)
                    .header("Content-type", type)
                    .put(personJSONMod);

        PersonBean person = response.readEntity(PersonBean.class);

        printOnScreenHeader(3, "PUT", base_URL + "person/" + first_person_id.getId(),
                type, type, response);

        printBody(type, person);


        if (!first_person_id.getFirstname().equals(person.getFirstname()) && (response.getStatus() == 202 || response.getStatus() == 200)) {
            System.out.println("OK");
            first_person_id = person;
            return 0;
        } else {
            System.out.println("ERROR");
            return 1;
        }
    }

    private static int doRequest4(WebTarget service, String type) throws NullPointerException {
        Response response = null;
        if (type.equals(MediaType.APPLICATION_XML))
            response = service.path("person/").request()
                    .accept(type)
                    .header("Content-type", type)
                    .post(personXMLAdd);
        else
            response = service.path("person/").request()
                    .accept(type)
                    .header("Content-type", type)
                    .post(personJSONAdd);

        PersonBean person = response.readEntity(PersonBean.class);

        printOnScreenHeader(4, "POST", base_URL + "person/",
                type, type, response);

        printBody(type, person);

        if (person.getId() != null && (response.getStatus() == 202 || response.getStatus() == 200)) {
            System.out.println("OK");
            addedtoDelete = person;
            return 0;
        } else
            System.out.println("ERROR");
        return 1;
    }

    private static Integer doRequest5(WebTarget service, String type) throws NullPointerException {
        Response response = null;
        if (type.equals(MediaType.APPLICATION_XML))
            response = service.path("person/" + addedtoDelete.getId()).request()
                    .accept(type)
                    .header("Content-type", type)
                    .delete();
        else
            response = service.path("person/" + addedtoDelete.getId()).request()
                    .accept(type)
                    .header("Content-type", type)
                    .delete();

        printOnScreenHeader(5, "DELETE", base_URL + "person/" + addedtoDelete.getId(),
                type, type, response);


        Response responseGet = service.path("person/" + addedtoDelete.getId())
                .request()
                .accept(type)
                .header("Content-type", type)
                .get();
        printOnScreenHeader(5, "GET", base_URL + "person/" + addedtoDelete.getId(),
                type, type, responseGet);

        if (responseGet.getStatus() >= 400 && (response.getStatus() >= 200 || response.getStatus() < 300)) {
            System.out.println("OK");
            return 0;
        } else
            System.out.println("ERROR");
        return 1;
    }

    private static Integer doRequest6(WebTarget service, String type) throws NullPointerException {
        Response response = service.path("measureTypes").request()
                .accept(type)
                .header("Content-type", type)
                .get();

        WrapperMeasureTypes measures = response.readEntity(WrapperMeasureTypes.class);

        printOnScreenHeader(6, "GET", base_URL + "measureTypes",
                type, type, response);

        measure_types = measures.list;
        printBody(type, measures);

        if (measures.list.size() <= 2) {
            System.out.println("ERROR");
            return 1;
        } else
            System.out.println("OK");
        return 0;
    }

    private static Integer doRequest7(WebTarget service, String type) throws NullPointerException {
        Response response = null;
        Integer counter = 0;

        WrapperMeasureHistory measuresFirst;
        WrapperMeasureHistory measuresLast;


        for (String measure : measure_types) {

            response = service.path("person/" + first_person_id.getId() + "/" + measure)
                    .request()
                    .accept(type)
                    .header("Content-type", type)
                    .get();
            measuresFirst = response.readEntity(WrapperMeasureHistory.class);

            printOnScreenHeader(7, "GET", base_URL + "person/" + first_person_id.getId() + "/" + measure,
                    type, type, response);
            printBody(type, measuresFirst);

            if (!measuresFirst.list.isEmpty())
                saveOneMeasure(measuresFirst, measure, first_person_id.getId());

            counter += measuresFirst.list.size();

        }

        if (counter > 0) {
            System.out.println("OK");
            return 0;
        } else
            System.out.println("ERROR");
        return 1;


    }

    private static Integer doRequest8(WebTarget service, String type) throws NullPointerException {

        Response response = service.path("person/" + id38 + "/" + measureTypeSaved38 + "/" + measureSaved38.getMid())
                .request()
                .accept(type)
                .header("Content-type", type)
                .get();

        MeasureHistoryBean measure = response.readEntity(MeasureHistoryBean.class);
        printOnScreenHeader(8, "GET", base_URL + "person/" + id38 + "/" + measureTypeSaved38 + "/" + measureSaved38.getMid(),
                type, type, response);
        printBody(type, measure);

        if (response.getStatus() >= 200 || response.getStatus() < 300) {
            System.out.println("OK");
            return 0;
        } else
            System.out.println("ERROR");
        return 1;
    }

    private static Integer doRequest9(WebTarget service, String type) throws NullPointerException {

        Response responseGet = service.path("person/" + first_person_id.getId() + "/" + "weight/")
                .request()
                .accept(type)
                .header("Content-type", type)
                .get();

        WrapperMeasureHistory measures = responseGet.readEntity(WrapperMeasureHistory.class);
        Integer count = measures.list.size();

        Response response = null;
        if (type.equals(MediaType.APPLICATION_XML))
            response = service.path("person/" + first_person_id.getId() + "/" + "weight/").request()
                    .accept(type)
                    .header("Content-type", type)
                    .post(measureXMLAdd);
        else
            response = service.path("person/" + first_person_id.getId() + "/" + "weight/").request()
                    .accept(type)
                    .header("Content-type", type)
                    .post(measureJSONAdd);

        MeasureHistoryBean measure = response.readEntity(MeasureHistoryBean.class);
        printOnScreenHeader(9, "POST", "person/" + first_person_id.getId() + "/" + "weight/",
                type, type, response);
        printBody(type, measure);

        measureAdded = measure;

        responseGet = service.path("person/" + first_person_id.getId() + "/" + "weight/")
                .request()
                .accept(type)
                .header("Content-type", type)
                .get();

        measures = responseGet.readEntity(WrapperMeasureHistory.class);

        if (count < measures.list.size() && measure != null && (response.getStatus() >= 200 || response.getStatus() < 300)) {
            System.out.println("OK");
            return 0;
        } else
            System.out.println("ERROR");
        return 1;
    }

    private static Integer doRequest10(WebTarget service, String type) throws NullPointerException {

        Response response = null;
        if (type.equals(MediaType.APPLICATION_XML))
            response = service.path("person/" + first_person_id.getId() + "/weight/" + measureAdded.getMid()).request()
                    .accept(type)
                    .header("Content-type", type)
                    .put(measureXMLPut);
        else
            response = service.path("person/" + first_person_id.getId() + "/weight/" + measureAdded.getMid()).request()
                    .accept(type)
                    .header("Content-type", type)
                    .put(measureJSONPut);

        MeasureHistoryBean measure = response.readEntity(MeasureHistoryBean.class);
        printOnScreenHeader(10, "PUT", "person/" + first_person_id.getId() + "/weight/" + measureAdded.getMid(),
                type, type, response);
        printBody(type, measure);


        response = service.path("person/" + first_person_id.getId() + "/weight/")
                .request()
                .accept(type)
                .header("Content-type", type)
                .get();
        WrapperMeasureHistory measures = response.readEntity(WrapperMeasureHistory.class);

        for (MeasureHistoryBean measureBean : measures.list) {
            if ((measureBean.getMid() == measureAdded.getMid()) && (measureBean.getValue().equals("90"))) {
                System.out.println("OK");
                return 0;
            }
        }

        System.out.println("ERROR");
        return 1;
    }

    private static Integer doRequest11(WebTarget service, String type,
                                       String beforeDateGiven, String afterDateGiven) throws NullPointerException {
        Response response = service.path("person/" + first_person_id.getId() + "/weight/")
                .queryParam("before", beforeDateGiven)
                .queryParam("after", afterDateGiven).request()
                .accept(type).get();

        WrapperMeasureHistory results = response.readEntity(WrapperMeasureHistory.class);

        printOnScreenHeader(11, "GET", "person/" + first_person_id.getId()
                + "?before=" + beforeDateGiven + "&after=" + afterDateGiven,
                type, type, response);
        printBody(type, results);

        if (results.list != null && results.list.size() > 1) {
            System.out.println("OK");
            return 0;
        }
        System.out.println("ERROR");
        return 1;
    }

    private static Integer doRequest12(WebTarget service, String type, String measureType,
                                       String maxValue, String minValue) throws NullPointerException {
        Response response = service.path("person")
                .queryParam("measureType", measureType)
                .queryParam("max", maxValue)
                .queryParam("min", minValue).request()
                .accept(type).get();

        WrapperPeople results = response.readEntity(WrapperPeople.class);

        printOnScreenHeader(12, "GET", "person?measureType=" + type + "&max=" + maxValue + "&min=" + minValue,
                type, type, response);
        printBody(type, results);

        if (results.list != null && results.list.size() > 1) {
            System.out.println("OK");
            return 0;
        }
        System.out.println("ERROR");
        return 1;
    }

    private static void saveOneMeasure(WrapperMeasureHistory measuresFirst, String measure, Integer id) {
        measureSaved38 = measuresFirst.list.get(0);
        measureTypeSaved38 = measure;
        id38 = id;

    }

    public static void printBody(String type, Object givenObject) {
        if (type.equals(MediaType.APPLICATION_XML))
            printBodyXML(givenObject.getClass(), givenObject);
        else
            printBodyJSON(givenObject);

    }
}