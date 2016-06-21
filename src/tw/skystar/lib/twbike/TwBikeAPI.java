package tw.skystar.lib.twbike;

import tw.skystar.lib.twbike.impl.CityBikeAPI;
import tw.skystar.lib.twbike.impl.PBikeAPI;
import tw.skystar.lib.twbike.impl.YouBikeAPI;
import tw.skystar.lib.twbike.model.BikeStation;
import tw.skystar.lib.twbike.model.BikeStation.Region;

import java.util.List;

public class TwBikeAPI {
    public interface TwBikeAPICallBack{
        void onBikeStationLoaded(List<BikeStation> stations);
        void onBikeStationLoadFailed();
    }

    public static void loadBikeStations(Region region, TwBikeAPICallBack callBack){
        new Thread(){
            @Override
            public void run() {
                List<BikeStation> results;
                if(region == Region.KAOHSIUNG){
                    //CityBike
                    results = CityBikeAPI.getStationInfo();
                }else if(region == Region.PINGTUNG){
                    //PBike
                    results = PBikeAPI.getStationInfo();
                }else{
                    //YouBike
                    results = YouBikeAPI.getStationInfo(region);
                }
                if(results != null) {
                    //successful
                    callBack.onBikeStationLoaded(results);
                }else{
                    //failed
                    callBack.onBikeStationLoadFailed();
                }
            }
        }.start();
    }
}
