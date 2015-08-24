package com.alexandonian.batesconnect.parser;

import com.alexandonian.batesconnect.infoItems.EventItem;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * SAX tag handler
 *
 * @author Alex Andonian alexandonian@gmail.com
 */
public class EventParseHandler extends DefaultHandler {

    private List<EventItem> eventItems;

    // Used to reference item while parsing
    private EventItem currentItem;

    // Parsing title indicator
    private boolean parsingTitle;
    // Parsing description indication
    private boolean parsingDescription;
    // Parsing link indicator
    private boolean parsingLink;
    // Parsing pubDate indicator
    private boolean parsingPubDate;

    public EventParseHandler() {
        eventItems = new ArrayList<EventItem>();
    }

    public List<EventItem> getEventItems() {
        return eventItems;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        if ("item".equals(qName)) {
            currentItem = new EventItem();
        } else if ("title".equals(qName)) {
            parsingTitle = true;
        } else if ("description".equals(qName)) {
            parsingDescription = true;
        } else if ("link".equals(qName)) {
            parsingLink = true;
        } else if ("pubDate".equals(qName)) {
            parsingPubDate = true;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        if ("item".equals(qName)) {
            eventItems.add(currentItem);
            currentItem = null;
        } else if ("title".equals(qName)) {
            parsingTitle = false;
        } else if ("description".equals(qName)) {
            parsingDescription = false;
        } else if ("link".equals(qName)) {
            parsingLink = false;
        } else if ("pubDate".equals(qName)) {
            parsingLink = false;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        if (parsingTitle) {
            if (currentItem != null)
                currentItem.setTitle(new String(ch, start, length));
        } else if (parsingDescription) {
            if (currentItem != null)
                currentItem.setDescription(new String(ch, start, length));
        } else if (parsingLink) {
            if (currentItem != null)
                currentItem.setLink(new String(ch, start, length));
            parsingLink = false;
        }else if (parsingPubDate) {
            if (currentItem != null)
                currentItem.setPubDate(new String(ch, start, length));
            parsingPubDate = false;
        }
    }
}
