package com.example.teamcity.api;

import com.example.teamcity.api.constants.ErrorMessages;
import com.example.teamcity.api.enums.Role;
import com.example.teamcity.api.generators.RandomData;
import com.example.teamcity.api.generators.TestData;
import com.example.teamcity.api.generators.TestDataGenerator;
import com.example.teamcity.api.models.Project;
import com.example.teamcity.api.requests.checked.CheckedBuildConf;
import com.example.teamcity.api.requests.checked.CheckedProject;
import com.example.teamcity.api.requests.unchecked.UncheckedBuildConfig;
import com.example.teamcity.api.spec.Specifications;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

public class BuildConfigurationTest extends BaseApiTest {

    public Pair<TestData, Project> projectCreation(){
        var testData = testDataStorage.addTestData();
        checkedWithSuperUser.getUserRequest()
                .create(testData.getUser());

        var project = new CheckedProject(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(testData.getProject());

        return Pair.of(testData, project);
    }

    @Test
    public void basicBuildConfigurationTest() {
        var testData = testDataStorage.addTestData();
        testData.getUser().setRoles(TestDataGenerator.generateRoles(Role.PROJECT_ADMIN, "g"));
        checkedWithSuperUser.getUserRequest()
                .create(testData.getUser());

        var project = new CheckedProject(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(testData.getProject());

        softy.assertThat(project.getId()).isEqualTo(testData.getProject().getId());

        var buildConf = new CheckedBuildConf(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(testData.getBuildType());

        softy.assertThat(buildConf.getId()).isEqualTo(testData.getBuildType().getId());
    }

    @Test
    public void positiveBuildConfigurationCreationMaxAllowedName(){
        var projectCreation = projectCreation();
        var testData = projectCreation.getLeft();

        var buildConfDescription = testData.getBuildType();
        buildConfDescription.setName(RandomData.getString(250, RandomData.StringType.RANDOM_STRING));

        var buildConf = new CheckedBuildConf(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(buildConfDescription);

        softy.assertThat(buildConf.getId()).isEqualTo(testData.getBuildType().getId());
    }

    @Test
    public void positiveBuildConfigurationCreationMaxAllowedId(){
        var projectCreation = projectCreation();
        var testData = projectCreation.getLeft();

        var buildConfDescription = testData.getBuildType();
        buildConfDescription.setId(RandomData.getString(220, RandomData.StringType.RANDOM_STRING));

        var buildConf = new CheckedBuildConf(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(buildConfDescription);
        softy.assertThat(buildConf.getId()).isEqualTo(testData.getBuildType().getId());
    }

    @Test
    //Failing test.Expected status code <400> but was <500>.
    public void negativeBuildConfigurationCreationBoundaryValueId() {
        var projectCreation = projectCreation();
        var testData = projectCreation.getLeft();

        var buildConfDescription = testData.getBuildType();
        buildConfDescription.setId(RandomData.getString(221, RandomData.StringType.RANDOM_STRING));

        new UncheckedBuildConfig(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(buildConfDescription)
                .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    public void negativeBuildConfigurationCreationEmptyName() {
        var projectCreation = projectCreation();
        var testData = projectCreation.getLeft();

        var buildConfDescription = testData.getBuildType();
        buildConfDescription.setName("");

        new UncheckedBuildConfig(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(buildConfDescription)
                .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(Matchers.containsString(ErrorMessages.BUILD_BAD_REQUEST_NAME_EMPTY));
    }

    @Test
    public void negativeBuildConfigurationWithoutId() {
        var projectCreation = projectCreation();
        var testData = projectCreation.getLeft();

        var buildConfDescription = testData.getBuildType();
        buildConfDescription.setId("");

        new UncheckedBuildConfig(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(buildConfDescription)
                .then().assertThat().statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                .body(Matchers.containsString(ErrorMessages.BUILD_SERVER_ERROR_ID_EMPTY));
    }

    @Test
    public void negativeBuildConfigCreationWithoutProject() {
        var projectCreation = projectCreation();
        var testData = projectCreation.getLeft();

        var buildConfDescription = testData.getBuildType();
        buildConfDescription.setProject(null);

        new UncheckedBuildConfig(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(buildConfDescription)
                .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(Matchers.containsString(ErrorMessages.BUILD_BAD_REQUEST_PROJECT_EMPTY));
    }

    @Test
    public void negativeProjectCreationDuplicateId() {
        var projectCreation = projectCreation();
        var testData = projectCreation.getLeft();
        String expectedMessage = String.format(ErrorMessages.BUILD_CONFIG_ALREADY_USED, testData.getBuildType().getId());

        new CheckedBuildConf(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(testData.getBuildType());

        new UncheckedBuildConfig(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(testData.getBuildType())
                .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(Matchers.containsString(expectedMessage));
    }


}
