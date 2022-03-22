package de.bail.classicmodels.graphql;

import com.google.gson.JsonObject;
import io.restassured.response.Response;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static io.restassured.RestAssured.given;

/**
 * Execute static graphql queries using the default endpoint: /graphql.
 * Return Rest Assured Response
 */
public class StaticGraphQLTest {

    /**
     * Execute static graphql queries from /test/resources/graphql/ with file extension '.gql'.
     * The default endpoint /graphql is used for all requests.
     * @param directory queries, mutations or subscription
     * @param query Query name: File name and query name should be named the same.
     * @param variables Json object with variables
     * @return Rest Assured Response
     */
    public Response queryGraphQL(String directory, String query, JsonObject variables) {
        // create payload
        JsonObject payload = new JsonObject();
        payload.addProperty("operationName", query);
        payload.addProperty("query", loadDocument(directory, query));
        payload.add("variables", variables);
        // request query
        return given()
                .body(payload.toString())
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .when().post("/graphql");
    }

    /**
     * Load query document form /test/resources/graphql/
     * @param directory queries, mutations or subscription
     * @param documentName document name without file extension '.gql'
     * @return
     */
    private String loadDocument(String directory, String documentName) {
        try {
            UriBuilder uriBuilder = UriBuilder.fromUri("/graphql/operations/");
            uriBuilder.path(directory)
                    .path(documentName + ".gql");
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(uriBuilder.build().toString());
            if (inputStream == null) {
                throw new IllegalArgumentException("query not found: " + uriBuilder.build().toString());
            } else {
                return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
