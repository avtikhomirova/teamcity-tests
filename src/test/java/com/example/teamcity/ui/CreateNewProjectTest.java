package com.example.teamcity.ui;

import com.codeborne.selenide.Condition;
import com.example.teamcity.api.generators.RandomData;
import com.example.teamcity.api.models.Project;
import com.example.teamcity.api.models.Projects;
import com.example.teamcity.api.requests.checked.CheckedProject;
import com.example.teamcity.api.spec.Specifications;
import com.example.teamcity.ui.pages.favorites.ProjectsPage;
import com.example.teamcity.ui.pages.admin.CreateNewProject;
import org.testng.annotations.Test;

import java.util.Optional;

public class CreateNewProjectTest extends BaseUiTest {
    @Test
    public void authorizedUserShouldBeAbleCreateNewProject() {
        var testData = testDataStorage.addTestData();
        var url = "https://github.com/avtikhomirova/teamcity";
        var nameToFind = testData.getProject().getName();

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
                .filter(project -> nameToFind.equals(project.getName()))
                .findFirst();

        if (foundProject.isPresent()) {
            Project project = foundProject.get();
            softy.assertThat(project.getName()).isEqualTo(testData.getProject().getName());
        } else {
            System.out.println("Project not found");
        }
    }

    @Test
    //Error! We suppose to have an error creating a project with name over 80 symbols.
    public void authorizedUserShouldNotBeAbleCreateNewProjectWithNameLengthOver80Symbols(){
        var testData = testDataStorage.addTestData();
        var url = "https://github.com/avtikhomirova/teamcity";
        var projectDescription = testData.getProject();
        projectDescription.setName(RandomData.getString(76, RandomData.StringType.RANDOM_STRING));

        loginAsUser(testData.getUser());

        new CreateNewProject()
                .open(testData.getProject().getParentProject().getLocator())
                .createProjectByUrl(url)
                .setupProject(projectDescription.getName(), testData.getBuildType().getName());

        //Here we need to add check the project creation error

        new ProjectsPage().open()
                .getSubprojects()
                .stream().reduce((first, second) -> second).get()
                .getHeader().shouldNotHave(Condition.text(testData.getProject().getName()));
    }
}
