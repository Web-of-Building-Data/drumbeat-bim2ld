package fi.aalto.cs.drumbeat.convert.bem2rdf;

import java.util.concurrent.ThreadLocalRandom;

import fi.aalto.cs.drumbeat.convert.bem2rdf.AppTest;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
//    	long trialNumber = 1000000000L;
////    	long trialNumber = 10L;
//    	for (int minLimit = 1; minLimit <= 6; ++minLimit) {
//	    	long sum = 0L;
//	    	for (long i = 0L; i < trialNumber; ++i) {
//	    		int result;
//	    		while ((result = ThreadLocalRandom.current().nextInt(1, 7)) < minLimit) {
//	    			sum -= 1L;
//	    		}
////	    		System.out.print(result);
//	    		sum += result;
//	    	}
//	    	System.out.printf("Stop when dice is >= %d --> %f%n", minLimit, ((double)sum) / trialNumber);
//    	}
    }
}
