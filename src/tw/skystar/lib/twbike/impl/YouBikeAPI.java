package tw.skystar.lib.twbike.impl;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import tw.skystar.lib.twbike.model.BikeStation;
import tw.skystar.lib.twbike.model.BikeStation.Region;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class YouBikeAPI {
    public static List<BikeStation> getStationInfo(Region region){
        String url = "http://i.youbike.com.tw/cht/f12.php?loc=";
        if(region == Region.TAIPEI) {
            url += "taipei";
        }else if(region == Region.NEW_TAIPEI) {
            url += "ntpc";
        }else if(region == Region.TAOYUAN) {
            url += "tycg";
        }else if(region == Region.HSINCHU) {
            url += "hccg";
        }else if(region == Region.TAICHUNG) {
            url += "taichung";
        }else if(region == Region.CHANGHUA) {
            url += "chcg";
        }else{
            return null;
        }
        ArrayList<BikeStation> results = new ArrayList<>();
        HttpGet httpGetRequest = new HttpGet(url);
        try{
            HttpResponse httpResponse = new DefaultHttpClient().execute(httpGetRequest);
            if(httpResponse.getStatusLine().getStatusCode()==200){
                final String result = EntityUtils.toString(httpResponse.getEntity());
                //SubString to JSON
                int start = result.indexOf("showmap");
                start = result.indexOf("'", start) + 1;
                int end = result.indexOf("'" , start + 1);
                String json = java.net.URLDecoder.decode(result.substring(start, end), "UTF-8");
                JSONObject stations = new JSONObject(json);
                Iterator<String> it = stations.keys();
                while(it.hasNext()){
                    JSONObject station = stations.getJSONObject(it.next());
                    BikeStation sta = new BikeStation();
                    sta.id = station.getInt("sno");
                    sta.name = station.getString("sna");
                    sta.location = station.getString("ar");
                    sta.totalSpace = Integer.parseInt(station.getString("tot"));
                    sta.availableCars = Integer.parseInt(station.getString("sbi"));
                    sta.availableSpace = Integer.parseInt(station.getString("bemp"));
                    sta.latitude = Double.parseDouble(station.getString("lat"));
                    sta.longitude = Double.parseDouble(station.getString("lng"));
                    sta.isOnService = station.getString("sv").equals("1");
                    sta.region = region;
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
