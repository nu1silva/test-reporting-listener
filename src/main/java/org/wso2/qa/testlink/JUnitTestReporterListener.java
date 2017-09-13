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

public class JUnitTestReporterListener extends RunListener {

    private static final Description FAILED = Description.createTestDescription("failed", "FAIL");
    private static final Description SKIPPED = Description.createTestDescription("skipped", "SKIP");

    @Override
    public void testRunFinished(Result result) throws Exception {
        System.out.println("Test run ended with [" + result.getRunCount() + "] tests");
    }

    @Override
    public void testFinished(Description description) throws Exception {
        if (description.getChildren().contains(FAILED)) {
            System.out.println(description.getTestClass() + "#" + description.getMethodName() + " | FAIL");
        } else if (description.getChildren().contains(SKIPPED)) {
            System.out.println(description.getTestClass() + "#" + description.getMethodName() + " | SKIP");
        } else {
            System.out.println(description.getTestClass() + "#" + description.getMethodName() + " | PASS");
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
