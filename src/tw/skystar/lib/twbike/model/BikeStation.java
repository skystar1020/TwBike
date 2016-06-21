package tw.skystar.lib.twbike.model;

public class BikeStation {
    public int id;
    public String name;
    public String location;
    public int totalSpace;     //總停車格
    public int availableCars;  //車輛數
    public int availableSpace; //可用停車格
    public double longitude, latitude;
    public boolean isOnService;
    public Region region;

    public enum Region{
        TAIPEI, NEW_TAIPEI, TAOYUAN, HSINCHU, TAICHUNG, CHANGHUA, KAOHSIUNG, PINGTUNG;
    }

    public String getMapImageUrl(String googleStaticMapsKey){
        StringBuilder sb = new StringBuilder();
        sb.append("http://maps.googleapis.com/maps/api/staticmap?center=").append(latitude).append(",").append(longitude)
                .append("6&zoom=13&size=600x300&maptype=roadmap&markers=color:red%7C").append(latitude).append(",")
                .append(longitude).append("&key=").append(googleStaticMapsKey);
        return sb.toString();
    }
}
