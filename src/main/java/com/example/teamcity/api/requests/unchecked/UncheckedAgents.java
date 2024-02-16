package com.example.teamcity.api.requests.unchecked;

import com.example.teamcity.api.requests.CrudInterface;
import com.example.teamcity.api.requests.Request;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class UncheckedAgents extends Request implements CrudInterface {

    private static final String AGENTS_ENDPOINT = "/app/rest/agents";

    public UncheckedAgents(RequestSpecification spec) {
        super(spec);
    }

    @Override
    public Object create(Object obj) {
        return null;
    }

    @Override
    public Response get(String id) {
        return null;
    }

    @Override
    public Object update(String id, Object obj) {
        return null;
    }

    @Override
    public Object delete(String id) {
        return null;
    }

    public Response getAllUnauthorizedAgents(){
        return given().spec(spec)
                .get(AGENTS_ENDPOINT + "?locator=enabled:true,authorized:false");
    }
    public Response getAllAuthorizedAgents(){
        return given().spec(spec)
                .get(AGENTS_ENDPOINT + "?locator=connected:true,authorized:true");
    }

    public Response updateAgentAuthorizationToTrue(String name){
        return given().spec(spec)
                .contentType("text/plain")
                .accept("text/plain")
                .body(String.valueOf(true))
                .put(AGENTS_ENDPOINT + "/" + name + "/authorized");
    }


}
