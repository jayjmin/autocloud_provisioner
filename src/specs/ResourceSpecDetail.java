package specs;
public class ResourceSpecDetail implements ResourceSpec {

    private double cpu_core = 0;
    private double ram_MB = 0;
    private double disk_GB = 0;
    
    public ResourceSpecDetail(double cpu, double ram, double disk) {
        this.setResource(cpu, ram, disk);
    }
    public void setResource(String resourceId)
    {}
    public void setResource(double minCpu, double minRamMB, double minDiskGB)
    {
        this.cpu_core = minCpu;
        this.ram_MB = minRamMB;
        this.disk_GB = minDiskGB;
    }

    public String getResourceId()
    {return null;}
    public double getResourceCpu()
    {return this.cpu_core;}
    public double getResourceRam()
    {return this.ram_MB;}
    public double getResourceDisk()
    {return this.disk_GB;}

    public String toString()
    {
        StringBuilder s = new StringBuilder();
        s.append("Resource:cpu= "+ this.cpu_core + " ,ram= " + this.ram_MB + " ,disk= "+this.disk_GB);
        return s.toString();
    }
}
