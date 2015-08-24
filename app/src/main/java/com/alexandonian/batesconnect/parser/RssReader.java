package com.alexandonian.batesconnect.parser;

import com.alexandonian.batesconnect.infoItems.EventItem;

import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by Administrator on 8/10/2015.
 */
public class RssReader {

    private String rssUrl;

    public RssReader(String rssUrl) {
        this.rssUrl = rssUrl;
    }

    public List<EventItem> getItems() throws Exception {

        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();

        EventParseHandler handler = new EventParseHandler();

        saxParser.parse(rssUrl, handler);

        return handler.getEventItems();

    }
}
