package com.example.teamcity.api;

import com.example.teamcity.api.constants.ErrorMessages;
import com.example.teamcity.api.enums.Role;
import com.example.teamcity.api.generators.RandomData;
import com.example.teamcity.api.generators.TestDataGenerator;
import com.example.teamcity.api.requests.checked.CheckedProject;
import com.example.teamcity.api.requests.unchecked.UncheckedProject;
import com.example.teamcity.api.spec.Specifications;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

public class ProjectCreateTest extends BaseApiTest {

    @Test
    public void basicProjectCreationProjectAdmin() {
        var testData = testDataStorage.addTestData();
        testData.getUser().setRoles(TestDataGenerator.generateRoles(Role.PROJECT_ADMIN, "g"));
        checkedWithSuperUser.getUserRequest()
                .create(testData.getUser());

        var project = new CheckedProject(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(testData.getProject());

        softy.assertThat(project.getId()).isEqualTo(testData.getProject().getId());
    }

    @Test
    public void positiveProjectCreationMaxAllowedName() {
        var testData = testDataStorage.addTestData();
        checkedWithSuperUser.getUserRequest()
                .create(testData.getUser());

        var projectDescription = testData.getProject();
        projectDescription.setName(RandomData.getString(75, RandomData.StringType.RANDOM_STRING));

        var project = new CheckedProject(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(projectDescription);

        softy.assertThat(project.getId()).isEqualTo(projectDescription.getId());
        softy.assertThat(project.getName()).isEqualTo(projectDescription.getName());
    }

    @Test
    public void positiveProjectCreationMaxAllowedId() {
        var testData = testDataStorage.addTestData();
        checkedWithSuperUser.getUserRequest()
                .create(testData.getUser());

        var projectDescription = testData.getProject();
        projectDescription.setId(RandomData.getString(220, RandomData.StringType.RANDOM_STRING));

        var project = new CheckedProject(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(projectDescription);

        softy.assertThat(project.getId()).isEqualTo(projectDescription.getId());
    }

    @Test
    public void positiveProjectCreationUnderParentProject() {
        var parentTestData = testDataStorage.addTestData();
        checkedWithSuperUser.getUserRequest()
                .create(parentTestData.getUser());

        var parentProject = new CheckedProject(Specifications.getSpec()
                .authSpec(parentTestData.getUser()))
                .create(parentTestData.getProject());

        var childTestData = testDataStorage.addTestData();
        var childProjectDescription = childTestData.getProject();
        childProjectDescription.setParentProject(parentProject);

        var childPproject = new CheckedProject(Specifications.getSpec()
                .authSpec(parentTestData.getUser()))
                .create(childProjectDescription);

        softy.assertThat(parentProject.getId()).isEqualTo(parentProject.getId());
        softy.assertThat(childPproject.getId()).isEqualTo(childProjectDescription.getId());
    }

    @Test
    //Failing test.Expected status code <400> but was <500>.
    public void negativeProjectCreationBoundaryValueId() {
        var testData = testDataStorage.addTestData();
        checkedWithSuperUser.getUserRequest()
                .create(testData.getUser());

        var projectDescription = testData.getProject();
        projectDescription.setId(RandomData.getString(221, RandomData.StringType.RANDOM_STRING));

        new UncheckedProject(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(projectDescription)
                .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    public void negativeProjectCreationEmptyName() {
        var testData = testDataStorage.addTestData();
        testData.getUser();
        checkedWithSuperUser.getUserRequest()
                .create(testData.getUser());

        var projectDescription = testData.getProject();
        projectDescription.setName("");

        new UncheckedProject(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(projectDescription)
                .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(Matchers.containsString(ErrorMessages.PROJECT_BAD_REQUEST_NAME_EMPTY));
    }

    @Test
    public void negativeProjectCreationWithoutId() {
        var testData = testDataStorage.addTestData();
        testData.getUser();
        checkedWithSuperUser.getUserRequest()
                .create(testData.getUser());

        var projectDescription = testData.getProject();
        projectDescription.setId("");

        new UncheckedProject(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(projectDescription)
                .then().assertThat().statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                .body(Matchers.containsString(ErrorMessages.PROJECT_SERVER_ERROR_ID_EMPTY));
    }

    @Test
    //Failing test.Expected status code <500> but was <200>.
    public void negativeProjectCreationInvalidParentProjectId() {
        var testData = testDataStorage.addTestData();
        testData.getUser();
        checkedWithSuperUser.getUserRequest()
                .create(testData.getUser());

        var projectDescription = testData.getProject();
        projectDescription.setParentProject(null);

        new UncheckedProject(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(projectDescription)
                .then().assertThat().statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    @Test
    public void negativeProjectCreationDuplicateName() {
        var testData = testDataStorage.addTestData();
        checkedWithSuperUser.getUserRequest()
                .create(testData.getUser());

        new CheckedProject(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(testData.getProject());

        new UncheckedProject(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(testData.getProject())
                .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(Matchers.containsString(ErrorMessages.PROJECT_BAD_REQUEST_NAME_DUPLICATE));

    }

}
