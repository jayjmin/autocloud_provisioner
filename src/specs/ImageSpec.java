package specs;
import org.jclouds.compute.domain.OsFamily;

public interface ImageSpec {
    public void setImage(String imageId);
    public void setImage(String osName, String osVersion, String osBits);

    public String getImageId();
    public OsFamily getOsFamily();
    public String getOsVersion();
    public boolean getOs64Bit();
}
