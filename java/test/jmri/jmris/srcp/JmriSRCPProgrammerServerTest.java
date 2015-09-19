//JmriSRCPProgrammerServerTest.java

package jmri.jmris.srcp;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Tests for the jmri.jmris.srcp.JmriSRCPProgrammerServer class
 * @author                      Paul Bender
 * @version                     $Revision$
 */
public class JmriSRCPProgrammerServerTest extends TestCase {

    public void testCtor() {
	java.io.DataOutputStream output=new java.io.DataOutputStream(System.out);
        JmriSRCPProgrammerServer a = new JmriSRCPProgrammerServer(output);
        Assert.assertNotNull(a);
    }

    // from here down is testing infrastructure

    public JmriSRCPProgrammerServerTest(String s) {
        super(s);
    }

    // Main entry point
    static public void main(String[] args) {
        String[] testCaseName = {SRCPTest.class.getName()};
        junit.swingui.TestRunner.main(testCaseName);
    }

    // test suite from all defined tests
    public static Test suite() {
        TestSuite suite = new TestSuite(jmri.jmris.srcp.JmriSRCPProgrammerServerTest.class);

        return suite;
    }

    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(JmriSRCPProgrammerServerTest.class.getName());

}
