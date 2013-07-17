package org.gnuton.newshub.utils;

import junit.framework.TestCase;

/**
 * Created by gnuton on 6/24/13.
 */
public class XMLFeedParserTest extends TestCase {
    private XMLFeedParser mParser;
    public void setUp() throws Exception {
        super.setUp();
        mParser = new XMLFeedParser(null);
    }

    public void tearDown() throws Exception {

    }

    public void testParseXML() throws Exception {
        assertEquals(1,0);
    }


}
