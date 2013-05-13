package specs;
public interface ResourceSpec {
    public void setResource(String resourceId);
    public void setResource(double minCpu, double minRamMB, double minDiskGB);

    public String getResourceId();
    public double getResourceCpu();
    public double getResourceRam();
    public double getResourceDisk();
}
