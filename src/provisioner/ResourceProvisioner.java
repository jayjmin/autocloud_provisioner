package provisioner;
import org.jclouds.compute.*;
import org.jclouds.compute.domain.*;

import provider.ProviderCredential;
import specs.SpecParser;

import java.util.*;

public class ResourceProvisioner {

    public static String GROUP_NAME = "nogroup";
    private Map<String, ProviderCredential> providers = null;
    private List<CloudConfiguration> specs = null;

    private List<NodeMetadata> createdNodes = new ArrayList<NodeMetadata>();
            
    public ResourceProvisioner(String providerFile, String specFile)
    {
        providers = provider.ProviderCredentialParser.parseProvider(providerFile);
        specs = SpecParser.parseCloudSpec(specFile, providers);
    }
    
    public void start()
    {
        System.out.println("Starting provisioning...");
        for(int i=0; i<specs.size(); i++)
        {
            CloudConfiguration spec = specs.get(i);
            if(spec.getNumMachines() <= 0)
                continue;

            Set<? extends NodeMetadata> nodes = spec.createMachines();
            
            int numDifferences = spec.getNumMachines() - nodes.size();
            
            if(numDifferences > 0)
            {
                System.err.println("Cannot allocate enough resource");
                if(spec.isNeedAllocateOnce())
                {
                    // All machines should be created in one provider, once
                    // Delete created machines
                    // Try to find it on next specs
                    System.err.println("Need to allocate once! destroy created nodes");
                    destroyNodes(spec.getComputeService(), nodes);
                    numDifferences = spec.getNumMachines();
                }
                else
                {
                    // Try to find remains on next specs
                    this.createdNodes.addAll(nodes);
                }
                
                if(i+1 < specs.size())
                {
                    CloudConfiguration nextSpec = specs.get(i+1);
                    nextSpec.setNumMachines(nextSpec.getNumMachines() + numDifferences);
                    System.err.println("Added more machines to the next spec" + numDifferences);
                }
            }
            else
            {
                this.createdNodes.addAll(nodes);
            }
        }
            
        System.out.println("Finished provisioning...");
    }
    private void destroyNodes(ComputeService client, Set<? extends NodeMetadata> nodes)
    {
        System.out.println("Destroying nodes...");
        for(NodeMetadata node:nodes)
        {
            client.destroyNode(node.getId());
        }
        System.out.println("Finished destroying nodes.");
    }
    
    public void printDebug()
    {
        System.err.println("======Providers=====");
        for(ProviderCredential pr:providers.values())
            System.err.println(pr);
        
        System.err.println("======Cloud Specs=====");
        for(CloudConfiguration sp:specs)
            System.err.println(sp);
        
        System.err.println("======CreatedNodes=====");
        debugPrintNodes(this.createdNodes);
    }
    
    public static void debugPrintNodes(Collection<? extends NodeMetadata> nodes)
    {
        System.out.println("Detail information of nodes... num="+ nodes.size());
        for (NodeMetadata node : nodes) {
            System.out.println(node.toString());
        }
    }
    
    public static void debugPrintImages(Set<? extends Image> images)
    {
        System.out.println("Num of images = "+ images.size());
        for(Image im:images)
        {
            System.out.println("+Image ID :"+im.getId());
            System.out.println("|Os Name :" + im.getOperatingSystem().getName()); 
            System.out.println("|Os Ver :" + im.getOperatingSystem().getVersion()); 
            System.out.println("|Os Arch :" + im.getOperatingSystem().getArch()); 
            System.out.println("|Os Family :" + im.getOperatingSystem().getFamily()); 
            System.out.println("|Os Desc :" + im.getOperatingSystem().getDescription()); 
            System.out.println("+Image ver :"+im.getVersion());
        }
        
    }

    public void printCreatedNodes()
    {
        System.out.println("======Created Nodes=====");
        debugPrintNodes(this.createdNodes);
    }

    public static void main(String [] args)
    {
        if(args.length < 2)
        {
            printUsage();
            return;
        }
        ResourceProvisioner c = new ResourceProvisioner(args[0], args[1]);
        c.start();
        //c.printCreatedNodes();
    }
    
    private static void printUsage()
    {
        System.out.println("Usage: java ResourceProvisioner provider_credential cloud_spec_list");
        System.out.println(" provider_credential should be formatted as Provider:Id:Password");
        System.out.println(" cloud_spec_list should be formatted as Provider:Resource:Location:Image(OS):NumMachine:IsAllocateOnce");
        System.out.println("   Resource can be either \"resource set ID\" or \"CPU(cores), RAM(MB), Disk(GB)");
        System.out.println("   Image can be either \"Image ID\" or \"OS name, OS version, Os architecture");
        System.out.println("   IsAllocateOnce should be 1 if you want to allocate all NumMachines in one provider");
        System.out.println();
        
        System.out.println("Ex. provider_credential:");
        System.out.println("aws-ec2:USERUSERUESRUSER:PASSWORDPASSWORDPASSWORDPASSWORD");
        System.out.println();
        
        System.out.println("Ex. cloud_spec_list:");
        System.out.println("aws-ec2:t1.micro:ap_southeast-2a:ami-e2ba2cd8:5:1");
        System.out.println("aws-ec2:2,1024,8000::Ubuntu,12.04,64:10:0");
    }
    
}
