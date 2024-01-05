package com.example.teamcity.ui.pages.project;


import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.example.teamcity.ui.Selectors;
import com.example.teamcity.ui.elements.BuildTypeElement;
import com.example.teamcity.ui.pages.Page;

import java.util.List;

import static com.codeborne.selenide.Selenide.elements;

public class ProjectPage extends Page {
    private static final String PROJECT_URL = "/project/";
    private ElementsCollection buildTypes = elements(Selectors.byClass("BuildTypes__item--UX"));

    public ProjectPage open(String projectName){
        Selenide.open(PROJECT_URL + projectName);
        waitUntilPageContentIsLoaded();
        waitUntilPageIsLoaded2();
        return this;
    }

    public List<BuildTypeElement> getBuildTypes(){
        buildTypes.shouldHave(CollectionCondition.sizeGreaterThan(0));
        return generatePageElements(buildTypes, BuildTypeElement::new);

    }


}
