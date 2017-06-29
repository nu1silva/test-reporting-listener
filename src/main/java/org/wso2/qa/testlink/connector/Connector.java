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
package org.wso2.qa.testlink.connector;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.qa.testlink.Util;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * This class is used as a connector with the database
 * to insert test results.
 */
public class Connector {

    private Util util = new Util();
    private final Logger logger = LoggerFactory.getLogger(Connector.class);

    /**
     * Insert results to the unit_tests table.
     *
     * @param component     name of the component which the test results will be updated
     * @param version       version of the component which the test results will be updated
     * @param buildNo       build number of the current jenkins build
     * @param platform      platform spec which the tests were executed on
     * @param test          name of the test should include package name.class name#method name
     * @param executionTime time taken to execute the test
     * @param status        status of test execution eg: PASS/FAIL
     * @throws ClassNotFoundException throws ClassNotFoundException
     * @throws SQLException           throws SQLException
     * @throws IOException            throws IOException
     */
    public void insertTestData(String component, String version, int buildNo, String platform, String test,
                               long executionTime, String status)
            throws ClassNotFoundException, SQLException, IOException {
        Connection conn = null;
        Statement stmt = null;

        try {
            Class.forName(util.getProperty("connector.jdbc.driver"));

            conn = (Connection) DriverManager.getConnection(util.getProperty("connector.jdbc.url"),
                    util.getProperty("connector.jdbc.username"),
                    util.getProperty("connector.jdbc.password"));
            stmt = (Statement) conn.createStatement();

            String query = "INSERT INTO testResults VALUES('" + component + "', '" + version + "',  " + buildNo + ", '"
                    + test + "', '" + platform + "', NOW(), '" + executionTime + "', '" + status + "')";

            if (logger.isDebugEnabled()) {
                logger.debug("executing ==> " + query);
            }
            stmt.executeUpdate(query);

        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }
}
