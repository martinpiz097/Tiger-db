package org.tigerdb.test;


import org.junit.AssumptionViolatedException;
import org.junit.Test;
import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.tigerdb.core.exceptions.TableAlreadyExistsException;
import org.tigerdb.core.model.Database;
import org.tigerdb.test.util.CallbackTask;
import org.tigerdb.test.util.StatisticsUtil;
import org.tigerdb.test.util.TimeUtil;

import java.io.File;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import static org.junit.Assert.*;

public class DatabaseTest {

    private static final String DB_NAME = "db_test_junit";
    private static final Logger logger = Logger.getLogger(DatabaseTest.class.getSimpleName());

    private static final int ELEMENT_COUNT = 1000;
    private static final String TEST_TABLE_NAME = "custom_table";

    private Stopwatch getDefaultStopwatch() {
        return new Stopwatch() {
            @Override
            protected void succeeded(long nanos, Description description) {
                logger.info("Task succeeded in "+
                        TimeUtil.nanosToSeconds(nanos)+" seconds");
            }

            @Override
            protected void failed(long nanos, Throwable e, Description description) {
                logger.severe("Task failed after "+
                        TimeUtil.nanosToSeconds(nanos)+" seconds");
            }

            @Override
            protected void skipped(long nanos, AssumptionViolatedException e, Description description) {
                logger.warning("Task skipped");
            }

            @Override
            protected void finished(long nanos, Description description) {
                //logger.info("Task finished in "+
                //        TimeUtil.nanosToSeconds(nanos)+" seconds");
            }
        };
    }

    private Statement getStatement(CallbackTask task) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                task.execute();
            }
        };
    }

    private Stopwatch executeTask(CallbackTask task) throws Throwable {
        Stopwatch stopwatch = getDefaultStopwatch();
        stopwatch.apply(getStatement(task),
                Description.createSuiteDescription(Database.class)).evaluate();
        return stopwatch;
    }

    private double getTaskTime(CallbackTask task) throws Throwable {
        return TimeUtil.millisToSeconds(executeTask(task).runtime(TimeUnit.MILLISECONDS));
    }

    @Test
    public void createDatabaseTimeTest() throws Throwable {
        CallbackTask task = ()  -> {
            Database database = new Database(DB_NAME);
        };
        executeTask(task);
    }

    @Test
    public void insertElementsTimeTest() throws Throwable {
        Database database = new Database(DB_NAME);
        try {
            database.createTable(TEST_TABLE_NAME, String.class);
        } catch (TableAlreadyExistsException e) {

        }

        CallbackTask task = () -> {
            for (int i = 0; i < ELEMENT_COUNT; i++) {
                database.insertInto(TEST_TABLE_NAME, "element "+i);
            }
        };

        logger.info(ELEMENT_COUNT +" elements inserted");
        logger.info(StatisticsUtil.elementsPerSeconds(ELEMENT_COUNT, getTaskTime(task)));

        database.drop();
    }

    @Test
    public void createTablesTest() throws Throwable {
        Database database = new Database(DB_NAME);
        CallbackTask task = () -> {
            for (int i = 0; i < ELEMENT_COUNT; i++) {
                database.createTable("table_name_"+i, String.class);
            }
        };
        double taskTime = getTaskTime(task);
        StatisticsUtil.elementsPerSeconds(ELEMENT_COUNT, taskTime);

        database.drop();
        assertFalse(new File(database.getStorePath()).exists());
    }

}
