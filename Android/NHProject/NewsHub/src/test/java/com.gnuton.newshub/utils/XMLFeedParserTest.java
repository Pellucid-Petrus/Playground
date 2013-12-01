package org.gnuton.newshub.utils;

import junit.framework.TestCase;

import org.gnuton.newshub.utils.XMLFeedParser;

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

    public void testATOM() {
        String test = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                " \n" +
                "<feed xmlns=\"http://www.w3.org/2005/Atom\">\n" +
                " \n" +
                "        <title>Example Feed</title>\n" +
                "        <subtitle>A subtitle.</subtitle>\n" +
                "        <link href=\"http://example.org/feed/\" rel=\"self\" />\n" +
                "        <link href=\"http://example.org/\" />\n" +
                "        <id>urn:uuid:60a76c80-d399-11d9-b91C-0003939e0af6</id>\n" +
                "        <updated>2003-12-13T18:30:02Z</updated>\n" +
                " \n" +
                " \n" +
                "        <entry>\n" +
                "                <title>Atom-Powered Robots Run Amok</title>\n" +
                "                <link href=\"http://example.org/2003/12/13/atom03\" />\n" +
                "                <link rel=\"alternate\" type=\"text/html\" href=\"http://example.org/2003/12/13/atom03.html\"/>\n" +
                "                <link rel=\"edit\" href=\"http://example.org/2003/12/13/atom03/edit\"/>\n" +
                "                <id>urn:uuid:1225c695-cfb8-4ebb-aaaa-80da344efa6a</id>\n" +
                "                <updated>2003-12-13T18:30:02Z</updated>\n" +
                "                <summary>Some text.</summary>\n" +
                "                <author>\n" +
                "                      <name>John Doe</name>\n" +
                "                      <email>johndoe@example.com</email>\n" +
                "                </author>\n" +
                "        </entry>\n" +
                " \n" +
                "</feed>";

        //XMLFeedParser parser = new XMLFeedParser();

    }

}
