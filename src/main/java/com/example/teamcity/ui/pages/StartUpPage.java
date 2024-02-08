package com.example.teamcity.ui.pages;


import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.example.teamcity.ui.Selectors;
import com.example.teamcity.ui.elements.PageElement;
import lombok.Getter;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.element;


@Getter

public class StartUpPage extends Page{
    private SelenideElement proceedButton = element(Selectors.byId("proceedButton"));
    private SelenideElement acceptLicense = element(Selectors.byId("accept"));
    private SelenideElement submitButton = element(Selectors.byType("submit"));
    private SelenideElement header = element(Selectors.byId("header"));

    public StartUpPage open(){
        Selenide.open("/mnt");
        return this;
    }

    public StartUpPage setupTeamCityServer(){
        waitUntilPageContentIsLoaded();
        proceedButton.click();
        waitUntilPageIsLoaded1();
        proceedButton.click();
        waitUntilPageContentIsLoaded();
        header.shouldBe(Condition.visible, Duration.ofSeconds(30));
        acceptLicense.shouldBe(Condition.enabled, Duration.ofSeconds(5));

        acceptLicense.scrollTo();
        acceptLicense.click();
        submitButton.click();
        return this;
    }

}
