package specs;
import org.jclouds.compute.domain.OsFamily;

public class ImageSpecDetail implements ImageSpec {
    private OsFamily osFamily = OsFamily.UBUNTU;
    private String osVersion = null;
    private boolean os64Bit = true;

    public ImageSpecDetail(String osName, String osVersion, String osBits) {
        this.setImage(osName, osVersion, osBits);
    }
    public void setImage(String imageId)
    {}
    public void setImage(String osName, String osVersion, String osBits)
    {
        this.osFamily = OsFamily.valueOf(OsFamily.class, osName.toUpperCase());
        this.osVersion = osVersion;
        
        if(osBits.contains("32"))
            this.os64Bit = false;
        else
            this.os64Bit = true;
    }

    public String getImageId() {return null;}
    public OsFamily getOsFamily() {return this.osFamily;}
    public String getOsVersion() {return this.osVersion;}
    public boolean getOs64Bit() {return this.os64Bit;}

    public String toString()
    {
        StringBuilder s = new StringBuilder();
        s.append("Image:OS= "+ this.osFamily + ", "+ this.osVersion + ", 64B =" +this.os64Bit);

        return s.toString();
    }
}
