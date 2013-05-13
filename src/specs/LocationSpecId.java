package specs;

public class LocationSpecId implements LocationSpec {
    private String locationId = null;
    
    public LocationSpecId(String image) {
        this.setLocation(image);
    }

    public void setLocation(String locationId)
    {
        this.locationId = locationId;
    }
    
    public void setLocation(String city, String continent) {}
    public String getLocationCity() {return null;}
    public String getLocationContinent() {return null;}
    
    public String getLocationId()
    { return this.locationId; }

    public String toString()
    {
        StringBuilder s = new StringBuilder();
        s.append("Location:ID= "+ this.locationId);
        return s.toString();
    }
}
