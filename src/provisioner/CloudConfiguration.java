package provisioner;
import java.util.Set;

import org.jclouds.compute.ComputeService;
import org.jclouds.compute.RunNodesException;
import org.jclouds.compute.domain.NodeMetadata;
import org.jclouds.compute.domain.TemplateBuilder;

import provider.ProviderCredential;

import specs.ImageSpec;
import specs.ResourceSpec;
import specs.LocationSpec;

public class CloudConfiguration {
    private ProviderCredential provider = null;
    private ResourceSpec resource = null;
    private LocationSpec location = null;
    private ImageSpec image = null;

    private int numMachines = 0;
    private boolean needAllocateOnce = false;
    
    private ComputeService client = null;

    public CloudConfiguration(String provider, String user, String password)
    {
        this.provider = new ProviderCredential(provider, user, password);
    }
    public CloudConfiguration(ProviderCredential provider)
    {
        this.provider = provider;
    }
    
    public void setResource(ResourceSpec rsrc)
    {
        this.resource = rsrc;
    }

    public void setLocation(LocationSpec location)
    {
        this.location = location;
    }

    public void setImage(ImageSpec image)
    {
        this.image = image;
    }
    
    public void setNumMachines(int numMachines)
    {
        this.numMachines = numMachines;
    }
    
    public int getNumMachines()
    {
        return this.numMachines;
    }
    
    public void setNeedAllocateOnce(boolean isNeedAllocateOnce)
    {
        this.needAllocateOnce = isNeedAllocateOnce;
    }
    
    public boolean isNeedAllocateOnce()
    {
        return this.needAllocateOnce;
    }
    
    public TemplateBuilder applyTemplate(TemplateBuilder tb)
    {
        System.out.println("Applying resource...");
        // Prioritise input values
        if(this.resource.getResourceId() != null && this.resource.getResourceId().length() != 0)
        {
            tb = tb.hardwareId(this.resource.getResourceId());
        }
        else
        {
            tb = tb.minCores(this.resource.getResourceCpu());
            tb = tb.minRam((int) this.resource.getResourceRam());
            tb = tb.minDisk(this.resource.getResourceDisk());
        }
        
        System.out.println("Applying location...");
        if(this.location.getLocationId() != null && this.location.getLocationId().length() != 0)
        {
            tb = tb.locationId(this.location.getLocationId());
        }

        System.out.println("Applying image...");
        if(this.image.getImageId() != null && this.image.getImageId().length() != 0)
        {
            tb = tb.imageId(this.image.getImageId());
        }
        else
        {
            tb.osFamily(this.image.getOsFamily());
            tb.os64Bit(this.image.getOs64Bit());
            if(this.image.getOsVersion() != null && this.image.getOsVersion().length() != 0)
                tb.osVersionMatches(this.image.getOsVersion());
        }
        
        return tb;
    }
    
    public Set<? extends NodeMetadata> createMachines()
    {
        if(numMachines <= 0)
            return null;
        
        System.out.println("Trying to create machines: "+this);
        
        this.client = provider.connectProvider();
        
        // Create a template
        TemplateBuilder tb = this.applyTemplate(client.templateBuilder());
        
        Set<? extends NodeMetadata> nodes = null;
        
        // Create a node
        try {
            System.out.println("Starting to request resources...");
            nodes = client.createNodesInGroup(ResourceProvisioner.GROUP_NAME, numMachines, tb.build());
            System.out.println(nodes.size() + " nodes are created from " + provider);
            ResourceProvisioner.debugPrintNodes(nodes);
        } catch (RunNodesException e) {
            System.err.println(e);
        }
        
        return nodes;
    }
    
    public ComputeService getComputeService()
    {
        return this.client;
    }

    public String toString()
    {
        StringBuilder s = new StringBuilder();
        s.append("("+this.provider.toString());
        s.append("|" + this.resource.toString());
        s.append("|" + this.location.toString());
        s.append("|" + this.image.toString());
        s.append("|NumInstance= " + this.numMachines);
        s.append("|AllocOnce= " + this.needAllocateOnce);
        s.append(")");
        return s.toString();
    }
}
