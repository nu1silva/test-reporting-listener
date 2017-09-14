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

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *  Main Junit Listener class
 *
 *  NOTE: At its very early stages.
 *
 */
public class JUnitTestReporterListener extends RunListener {

    private final Logger logger = LoggerFactory.getLogger(JUnitTestReporterListener.class);

    private static final Description FAILED = Description.createTestDescription("failed", "FAIL");
    private static final Description SKIPPED = Description.createTestDescription("skipped", "SKIP");

    @Override
    public void testRunFinished(Result result) throws Exception {
        logger.debug("Test run ended with [" + result.getRunCount() + "] tests");
    }

    @Override
    public void testFinished(Description description) throws Exception {
        if (description.getChildren().contains(FAILED)) {
            logger.info(description.getTestClass() + "#" + description.getMethodName() + " | FAIL");
        } else if (description.getChildren().contains(SKIPPED)) {
            logger.info(description.getTestClass() + "#" + description.getMethodName() + " | SKIP");
        } else {
            logger.info(description.getTestClass() + "#" + description.getMethodName() + " | PASS");
        }
    }

    @Override
    public void testFailure(Failure failure) throws Exception {
        failure.getDescription().addChild(FAILED);
    }

    @Override
    public void testIgnored(Description description) throws Exception {
        description.addChild(SKIPPED);
    }
}
