package com.example.teamcity.ui;

import com.codeborne.selenide.Condition;
import com.example.teamcity.api.requests.checked.CheckedAgents;
import com.example.teamcity.api.spec.Specifications;
import com.example.teamcity.ui.pages.StartUpPage;
import org.testng.annotations.Test;

import java.time.Duration;

import static org.awaitility.Awaitility.await;


public class SetupTest extends BaseUiTest{

    @Test
    public void startUpTest(){
        new StartUpPage()
                .open()
                .setupTeamCityServer()
                .getHeader()
                .shouldHave(Condition.text("Create Administrator Account"));
    }

    @Test
    public void setupTeamCityAgentTest(){
        //create new user and login data
        var testData = testDataStorage.addTestData();
        checkedWithSuperUser.getUserRequest()
                .create(testData.getUser());

        //get the list of unauthorized agents
        var allUnauthorizedAgents = new CheckedAgents(Specifications.getSpec().authSpec(testData.getUser()))
                .getAllUnauthorizedAgents();
        //make agent authorized
        var unauthorizedAgents = allUnauthorizedAgents.getAgent();
        softy.assertThat(unauthorizedAgents.size()).isGreaterThan(0);
        var agentName = new CheckedAgents(Specifications.getSpec().authSpec(testData.getUser())).updateAgentAuthorizationToTrue(unauthorizedAgents.get(0).getName());

        //check that the agent is authorized
        var allAuthorizedAgents = new CheckedAgents(Specifications.getSpec().authSpec(testData.getUser())).getAllAuthorizedAgents();
        var authorizedAgents = allAuthorizedAgents.getAgent();
        await().atMost(Duration.ofMinutes(5)).pollInterval(Duration.ofSeconds(5)).until(() -> authorizedAgents.size() == 1);
        softy.assertThat(authorizedAgents.get(0).getName()).isEqualTo(agentName);
    }
}
