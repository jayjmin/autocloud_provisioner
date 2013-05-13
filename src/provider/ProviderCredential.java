package provider;

import org.jclouds.ContextBuilder;
import org.jclouds.compute.ComputeService;
import org.jclouds.compute.ComputeServiceContext;


public class ProviderCredential {
    private String provider = null;
    private String user = null;
    private String password = null;
    
    public ProviderCredential(String provider, String user, String password)
    {
        this.provider = provider;
        this.user = user;
        this.password = password;
    }
    
    public String getProvider()
    {
        return provider;
    }

    public ComputeService connectProvider()
    {
        System.out.println("Connecting to the provider:" + provider);
        ContextBuilder builder = ContextBuilder.newBuilder(provider).credentials(user, password);
        ComputeService client = builder.buildView(ComputeServiceContext.class).getComputeService(); 

        System.out.println("Connected.");
        
        //System.out.println("Looking for instances already exist...");
        //ResourceProvisioner.debugPrintNodes(client.listNodes());

        //System.out.println("Looking for available images...");
        //ResourceProvisioner.debugPrintImages(client.listImages());
        
        return client;
    }
    
    public String toString()
    {
        return "Provider:"+provider + "/" + user;
    }
}
