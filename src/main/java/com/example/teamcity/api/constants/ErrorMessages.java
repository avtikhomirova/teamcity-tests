package com.example.teamcity.api.constants;


public abstract class ErrorMessages {
    public static final String PROJECT_UNAUTHORIZED = "Authentication required";
    public static final String PROJECT_BAD_REQUEST_NAME_EMPTY = "Project name cannot be empty";
    public static final String PROJECT_BAD_REQUEST_NAME_DUPLICATE = "Project with this name already exists";
    public static final String PROJECT_BAD_REQUEST_NOT_FOUND = "No project found by locator 'count:1,id:";
    public static final String PROJECT_SERVER_ERROR_ID_EMPTY = "Project ID must not be empty.";
    public static final String BUILD_BAD_REQUEST_NAME_EMPTY = "When creating a build type, non empty name should be provided";
    public static final String BUILD_BAD_REQUEST_PROJECT_EMPTY = "Build type creation request should contain project node";
    public static final String BUILD_CONFIG_ALREADY_USED = "The build configuration / template ID \"%s\" is already used by another configuration or template";
    public static final String BUILD_SERVER_ERROR_ID_EMPTY = "Build configuration or template ID must not be empty";

}
