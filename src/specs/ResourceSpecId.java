package specs;


public class ResourceSpecId implements ResourceSpec {
    private String resourceId = null;
    
    public ResourceSpecId(String hardware) {
        this.setResource(hardware);
    }
    public void setResource(String resourceId)
    {
        this.resourceId = resourceId;
    }
    public void setResource(double minCpu, double minRamMB, double minDiskGB)
    {}
    public String getResourceId()
    {
        return this.resourceId;
    }
    public double getResourceCpu()
    {return 0.0;}
    public double getResourceRam()
    {return 0.0;}
    public double getResourceDisk()
    {return 0.0;}

    public String toString()
    {
        StringBuilder s = new StringBuilder();
        s.append("Resource:ID= "+ this.resourceId);
        return s.toString();
    }
}
