package com.annakirillova.e2etests;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;

@Suite
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "com.annakirillova.e2etests.steps")
@SelectClasspathResource("features")
public class CucumberTests {
}
