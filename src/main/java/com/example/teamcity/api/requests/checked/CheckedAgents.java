package com.example.teamcity.api.requests.checked;

import com.example.teamcity.api.models.Agents;
import com.example.teamcity.api.requests.CrudInterface;
import com.example.teamcity.api.requests.Request;
import com.example.teamcity.api.requests.unchecked.UncheckedAgents;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;

import static io.restassured.RestAssured.given;

public class CheckedAgents extends Request implements CrudInterface {
    public CheckedAgents(RequestSpecification spec) {
        super(spec);
    }

    @Override
    public Object create(Object obj) {
        return null;
    }

    @Override
    public Object get(String id) {
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

    public Agents getAllUnauthorizedAgents() {
        return new UncheckedAgents(spec)
                .getAllUnauthorizedAgents()
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                .extract().as(Agents.class);
    }

    public Agents getAllAuthorizedAgents(){
        return new UncheckedAgents(spec)
                .getAllAuthorizedAgents()
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                .extract().as(Agents.class);
    }

    public ValidatableResponse updateAgentAuthorizationToTrue(String name) {
        return new UncheckedAgents(spec)
                .updateAgentAuthorizationToTrue(name)
                .then().assertThat().statusCode(HttpStatus.SC_OK);
    }
}
