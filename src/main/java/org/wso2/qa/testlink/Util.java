/**
 * Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 * <p>
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
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
import org.testng.ITestResult;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * This class is used to keep the utility functions
 * that are called within the listener.
 */
public class Util {

    private InputStream inputStream;

    private final Logger logger = LoggerFactory.getLogger(Util.class);

    /**
     * converts the integer value provided by TestNG
     * to PASS, FAIL or SKIPPED.
     *
     * @param resultString Integer value sent by TestNG that is mapped as test status
     * @return String Status that is compatible with Testlink
     */
    String resultStringConverter(int resultString) {
        switch (resultString) {
            case ITestResult.SUCCESS:
                return Constants.TL_STATUS_PASS;
            case ITestResult.FAILURE:
                return Constants.TL_STATUS_FAIL;
            case ITestResult.SKIP:
                return Constants.TL_STATUS_SKIP;
            case ITestResult.SUCCESS_PERCENTAGE_FAILURE:
                return Constants.TL_STATUS_SUCCESS_PERCENTAGE_FAILURE;
            case ITestResult.STARTED:
                return Constants.TL_STATUS_STARTED;
        }
        return null;
    }

    /**
     * retrieves the property values from the property file
     *
     * @param key the key value from the required property.
     * @return the property value for the given key
     * @throws IOException throws IOException
     */
    public String getProperty(String key) throws IOException {

        Properties properties = new Properties();
        inputStream = getClass().getClassLoader().getResourceAsStream("listener.properties");

        if (inputStream != null) {
            properties.load(inputStream);
        } else {
            throw new FileNotFoundException("property file listener.properties not found in the classpath");
        }
        inputStream.close();

        return properties.getProperty(key);
    }
}
