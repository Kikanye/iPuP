package comp3350.iPuP.tests.persistence;

import junit.framework.Test;
import junit.framework.TestSuite;

public class PersistenceTests
{
    private static TestSuite suite;

    public static Test suite()
    {
        suite = new TestSuite("Persistence Tests");
        suite.addTestSuite(DataAccessTest.class);
        return suite;
    }
}
