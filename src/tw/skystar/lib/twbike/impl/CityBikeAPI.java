package tw.skystar.lib.twbike.impl;

import org.xml.sax.InputSource;
import tw.skystar.lib.twbike.model.BikeStation;
import tw.skystar.lib.twbike.model.BikeStation.Region;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CityBikeAPI {
    public static List<BikeStation> getStationInfo(){
        ArrayList<BikeStation> results = new ArrayList<>();

        try{
            URL url = new URL("http://www.c-bike.com.tw/xml/stationlistopendata.aspx");
            InputSource is = new InputSource(new InputStreamReader(url.openStream(), "UTF-8"));
            is.setEncoding("UTF-8");
            XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
            XMLEventReader xmlEventReader = xmlInputFactory.createXMLEventReader(is.getCharacterStream());
            BikeStation sta = null;
            while(xmlEventReader.hasNext()){
                XMLEvent xmlEvent = xmlEventReader.nextEvent();
                if(xmlEvent.isStartElement()){
                    StartElement e = xmlEvent.asStartElement();
                    if(e.getName().getLocalPart().equals("Station")){
                        sta = new BikeStation();
                        results.add(sta);
                        sta.region = Region.KAOHSIUNG;
                        sta.isOnService = true;
                    }

                    if(e.getName().getLocalPart().equals("StationID")){
                        XMLEvent nextEvent = xmlEventReader.nextEvent();
                        sta.id = Integer.parseInt(nextEvent.asCharacters().getData());
                    }

                    if(e.getName().getLocalPart().equals("StationName")){
                        XMLEvent nextEvent = xmlEventReader.nextEvent();
                        sta.name = nextEvent.asCharacters().getData();
                    }

                    if(e.getName().getLocalPart().equals("StationAddress")){
                        XMLEvent nextEvent = xmlEventReader.nextEvent();
                        sta.location = nextEvent.asCharacters().getData();
                    }

                    if(e.getName().getLocalPart().equals("StationNums1")){
                        XMLEvent nextEvent = xmlEventReader.nextEvent();
                        sta.availableCars = Integer.parseInt(nextEvent.asCharacters().getData());
                    }

                    if(e.getName().getLocalPart().equals("StationNums2")){
                        XMLEvent nextEvent = xmlEventReader.nextEvent();
                        sta.availableSpace = Integer.parseInt(nextEvent.asCharacters().getData());
                    }

                    if(e.getName().getLocalPart().equals("StationLat")){
                        XMLEvent nextEvent = xmlEventReader.nextEvent();
                        sta.latitude = Double.parseDouble(nextEvent.asCharacters().getData());
                    }

                    if(e.getName().getLocalPart().equals("StationLon")){
                        XMLEvent nextEvent = xmlEventReader.nextEvent();
                        sta.longitude = Double.parseDouble(nextEvent.asCharacters().getData());
                    }
                }else if(xmlEvent.isEndElement()){
                    EndElement e = xmlEvent.asEndElement();
                    if(e.getName().getLocalPart().equals("Station")){
                        sta.totalSpace = sta.availableCars + sta.availableSpace;
                    }
                }
            }
            xmlEventReader.close();
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
        return results;
    }
}
