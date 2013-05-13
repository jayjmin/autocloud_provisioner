package specs;
public interface LocationSpec {
    public void setLocation(String locationId);
    public void setLocation(String city, String continent);
    public String getLocationId();
    public String getLocationCity();
    public String getLocationContinent();    
}
