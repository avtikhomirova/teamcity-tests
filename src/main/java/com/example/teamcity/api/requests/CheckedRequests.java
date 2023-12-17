package com.example.teamcity.api.requests;

import com.example.teamcity.api.requests.checked.CheckedBuildConf;
import com.example.teamcity.api.requests.checked.CheckedProject;
import com.example.teamcity.api.requests.checked.CheckedUser;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;

@Getter
public class CheckedRequests {
    private CheckedUser userRequest;
    private CheckedProject projectRequest;
    private CheckedBuildConf buildConfigRequest;
    public CheckedRequests(RequestSpecification spec){
        this.userRequest = new CheckedUser(spec);
        this.buildConfigRequest = new CheckedBuildConf(spec);
        this.projectRequest = new CheckedProject(spec);
    }

}
