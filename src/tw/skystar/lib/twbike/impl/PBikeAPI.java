package tw.skystar.lib.twbike.impl;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import tw.skystar.lib.twbike.model.BikeStation;
import tw.skystar.lib.twbike.model.BikeStation.Region;

import java.util.ArrayList;
import java.util.List;

public class PBikeAPI {
    public static List<BikeStation> getStationInfo(){
        ArrayList<BikeStation> results = new ArrayList<>();
        String url = "http://pbike.pthg.gov.tw/Station/Map.aspx";
        HttpGet httpGetRequest = new HttpGet(url);
        try{
            HttpResponse httpResponse = new DefaultHttpClient().execute(httpGetRequest);
            if(httpResponse.getStatusLine().getStatusCode()==200){
                final String result = EntityUtils.toString(httpResponse.getEntity());
                //SubString
                int index = result.indexOf("stationCoordinate") + 21;
                String strLocations = result.substring(index, result.indexOf('\"', index));
                index = result.indexOf("stationContent") + 18;
                String strStationInfos = result.substring(index, result.indexOf('\"', index));
                String[] stationInfos = strStationInfos.split("\\*");
                String[] locations = strLocations.split("\\*");

                for(int i=0;i<stationInfos.length;i++){
                    String[] stationInfo = stationInfos[i].split("#");
                    String[] location = locations[i].split(",");
                    BikeStation sta = new BikeStation();
                    sta.id = i + 1000;
                    sta.name = stationInfo[0];
                    sta.location = stationInfo[1];
                    sta.availableSpace = Integer.parseInt(stationInfo[2]);
                    sta.availableCars = Integer.parseInt(stationInfo[3]);
                    sta.totalSpace = sta.availableCars + sta.availableSpace;
                    sta.latitude = Double.parseDouble(location[0]);
                    sta.longitude = Double.parseDouble(location[1]);
                    sta.isOnService = true;
                    sta.region = Region.PINGTUNG;
                    results.add(sta);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
        return results;
    }
}
