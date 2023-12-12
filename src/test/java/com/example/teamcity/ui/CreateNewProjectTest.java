package com.example.teamcity.ui;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.selector.ByAttribute;
import com.example.teamcity.api.requests.checked.CheckedUser;
import com.example.teamcity.api.spec.Specifications;
import com.example.teamcity.ui.pages.LoginPage;
import com.example.teamcity.ui.pages.admin.CreateNewProject;
import org.testng.annotations.Test;

import static com.codeborne.selenide.Selenide.element;

public class CreateNewProjectTest extends BaseUiTest {
    @Test
    public void authorizedUserShouldBeAbleCreateNewProject() {
        var testData = testDataStorage.addTestData();
        var url = "https://github.com/avtikhomirova/teamcity";

        loginAsUser(testData.getUser());

        new CreateNewProject().open(testData.getProject().getParentProject().getLocator())
                .createProjectByUrl(url)
                .setupProject(testData.getProject().getName(), testData.getBuildType().getName());
    }
}
