CREATE DATABASE IF NOT EXISTS `testResults`;
USE `testResults`;

DROP TABLE IF EXISTS `testResults`;

CREATE TABLE `testResults` (
  `component`     VARCHAR(220) NOT NULL,
  `version`       VARCHAR(10)  NOT NULL,
  `buildNo`       INT(10)      NOT NULL,
  `test`          TEXT         NOT NULL,
  `platform`      TEXT         NOT NULL,
  `timestamp`     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `executionTime` LONG         NOT NULL,
  `status`        VARCHAR(25)  NOT NULL
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;