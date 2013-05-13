package specs;
import org.jclouds.compute.domain.OsFamily;

public class ImageSpecId implements ImageSpec {
    private String imageId = null;

    public ImageSpecId(String image) {
        this.setImage(image);
    }

    public void setImage(String imageId)
    {
        this.imageId = imageId;
    }
    
    public void setImage(String osName, String osVersion, String osBits)
    {}
    public String getImageId()
    { return this.imageId; }
    public OsFamily getOsFamily()
    { return null; }
    public String getOsVersion()
    { return null; }
    public boolean getOs64Bit()
    { return false; }

    public String toString()
    {
        StringBuilder s = new StringBuilder();
        s.append("Image:ID= "+ this.imageId);
        return s.toString();
    }
}
