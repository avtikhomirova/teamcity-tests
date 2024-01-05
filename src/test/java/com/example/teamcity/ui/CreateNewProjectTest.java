package com.example.teamcity.ui;

import com.codeborne.selenide.Condition;
import com.example.teamcity.api.models.Project;
import com.example.teamcity.api.models.Projects;
import com.example.teamcity.api.requests.checked.CheckedProject;
import com.example.teamcity.api.requests.unchecked.UncheckedProject;
import com.example.teamcity.api.spec.Specifications;
import com.example.teamcity.ui.pages.favorites.ProjectsPage;
import com.example.teamcity.ui.pages.admin.CreateNewProject;
import org.apache.http.HttpStatus;
import org.testng.annotations.Test;

import java.util.Optional;

public class CreateNewProjectTest extends BaseUiTest {
    private String url = "https://github.com/avtikhomirova/teamcity";
    @Test
    public void authorizedUserShouldBeAbleCreateNewProject() {
        var testData = testDataStorage.addTestData();

        loginAsUser(testData.getUser());

        new CreateNewProject()
                .open(testData.getProject().getParentProject().getLocator())
                .createProjectByUrl(url)
                .setupProject(testData.getProject().getName(), testData.getBuildType().getName());

        new ProjectsPage().open()
                .getSubprojects()
                .stream().reduce((first, second) -> second).get()
                .getHeader().shouldHave(Condition.text(testData.getProject().getName()));

        Projects allProjects = new CheckedProject(Specifications.getSpec().authSpec(testData.getUser()))
                .getAllProjects();

        Optional<Project> foundProject = allProjects.getProject().stream()
                .filter(project -> testData.getProject().getName().equals(project.getName()))
                .findFirst();

        var project = foundProject.get();

        new CheckedProject(Specifications.getSpec().authSpec(testData.getUser()))
                .get(project.getId());
        softy.assertThat(project.getId()).isNotEmpty();
        softy.assertThat(project.getName()).isEqualTo(testData.getProject().getName());
        softy.assertThat(project.getParentProjectId()).isEqualTo(testData.getProject().getParentProject().getLocator());

    }

    @Test
    public void authorizedUserShouldNotBeAbleCreateNewProjectWithoutName(){
        var testData = testDataStorage.addTestData();
        var projectDescription = testData.getProject();
        projectDescription.setName("");

        loginAsUser(testData.getUser());

        new CreateNewProject()
                .open(testData.getProject().getParentProject().getLocator())
                .createProjectByUrl(url)
                .setupProject(projectDescription.getName(), testData.getBuildType().getName());

        new UncheckedProject(Specifications.getSpec().authSpec(testData.getUser()))
                .get(projectDescription.getName())
                .then().assertThat().statusCode(HttpStatus.SC_NOT_FOUND);

        new ProjectsPage().open()
                .getSubprojects()
                .stream().reduce((first, second) -> second).get()
                .getHeader().shouldNotBe(Condition.exactText(testData.getProject().getName()));
    }
}
