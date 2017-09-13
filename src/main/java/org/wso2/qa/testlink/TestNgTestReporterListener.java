/*
 * Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.wso2.qa.testlink;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.IReporter;
import org.testng.IResultMap;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.xml.XmlSuite;
import org.wso2.qa.testlink.connector.Connector;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;


/**
 * Main TestNG listener class.
 */
public class TestNgTestReporterListener implements IReporter {

    private final Logger logger = LoggerFactory.getLogger(TestNgTestReporterListener.class);
    private Connector connector = new Connector();
    private Util util = new Util();
    private int buildNumber = 0;
    private String platform = null;

    /**
     * Using the listener iterates through each suite to get the results and update
     * the test database.
     *
     * @param xmlSuites test result suites
     * @param suites    test result suites
     * @param s         String s
     */
    @Override
    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String s) {

        logger.info("End of tests. publishing results to database.");

        for (ISuite suite : suites) {

            String componentName = suite.getParameter("component");
            String componentVersion = suite.getParameter("version");

            // Check for platform availability
            if (suite.getParameter("platform").contains("current.platform") ||
                    suite.getParameter("platform") == null) {
                platform = "DEFAULT";
            } else {
                platform = suite.getParameter("platform");
            }

            // Check for build number availability
            if (suite.getParameter("buildNumber").contains("current.build") ||
                    suite.getParameter("buildNumber") == null) {
                buildNumber = 1;
            } else {
                buildNumber = Integer.parseInt(suite.getParameter("buildNumber"));
            }


            if (logger.isDebugEnabled()) {
                logger.debug("component name : " + componentName);
                logger.debug("component version : " + componentVersion);
                logger.debug("current build : " + buildNumber);
                logger.debug("platform : " + platform);
            }


            if (!componentVersion.contains("SNAPSHOT")) {

                Map<String, ISuiteResult> tests = suite.getResults();

                Collection suiteResults = tests.values();
                ISuiteResult suiteResult = (ISuiteResult) suiteResults.iterator().next();
                ITestContext testContext = suiteResult.getTestContext();

                if (testContext.getPassedTests().getAllResults().size() > 0) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("storing passed test results to the database");
                    }
                    updateTestResults(testContext.getPassedTests(), componentName, componentVersion,
                            buildNumber, platform);
                }

                if (testContext.getFailedTests().getAllResults().size() > 0) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("storing failed test results to the database");
                    }
                    updateTestResults(testContext.getFailedTests(), componentName, componentVersion,
                            buildNumber, platform);
                }

                if (testContext.getSkippedTests().getAllResults().size() > 0) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("storing skipped test results to the database");
                    }
                    updateTestResults(testContext.getSkippedTests(), componentName, componentVersion,
                            buildNumber, platform);
                }
                logger.info("Result Publishing complete.");

            } else {
                logger.warn("Building a SNAPSHOT version. results will not be published");
            }
        }
    }


    /**
     * Generic method to help update the connector insert data method.
     *
     * @param testResultMap    map of results for tests
     * @param componentName    name of the component which the tests belong to
     * @param componentVersion version of the component which the tests belong to
     * @param buildNumber      current build number of the build
     */
    private void updateTestResults(IResultMap testResultMap, String componentName, String componentVersion,
                                   int buildNumber, String platform) {
        for (ITestResult testResults : testResultMap.getAllResults()) {

            if (logger.isDebugEnabled()) {
                logger.debug("storing results {" + componentName + ", " + componentVersion + ", " + buildNumber
                        + "" + ", " + platform + ", " + testResults.getMethod().getRealClass().getName() + "#"
                        + testResults.getName() + "," + util.resultStringConverter(testResults.getStatus()) + "}");
            }
            try {
                connector.insertTestData(componentName, componentVersion, buildNumber, platform,

                        testResults.getMethod().getRealClass().getName() + "#" + testResults.getName(),
                        testResults.getEndMillis() - testResults.getStartMillis(),
                        util.resultStringConverter(testResults.getStatus()));
            } catch (ClassNotFoundException | SQLException | IOException e) {
                logger.error(e.getLocalizedMessage(), e);
            }
        }
    }
}
