package specs;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import provider.ProviderCredential;
import provisioner.CloudConfiguration;



public class SpecParser {

    private static BufferedReader inputStream;


    private static class CloudSpecString
    {
        private String [] strs = null;

        private final static int PARSE_INDEX_SPEC_PROVIDER = 0;
        private final static int PARSE_INDEX_SPEC_HARDWARE = 1;
        private final static int PARSE_INDEX_SPEC_LOCATION = 2;
        private final static int PARSE_INDEX_SPEC_IMAGE = 3;
        private final static int PARSE_INDEX_SPEC_NUM = 4;
        private final static int PARSE_INDEX_SPEC_ALLOCONCE = 5;

        CloudSpecString(String line)
        {
            this.strs = line.trim().split(":");
        }
        String provider() {
            return strs[PARSE_INDEX_SPEC_PROVIDER];
        }
        String hardware() {
            return strs[PARSE_INDEX_SPEC_HARDWARE];
        }
        String location() {
            return strs[PARSE_INDEX_SPEC_LOCATION];
        }
        String image() {
            return strs[PARSE_INDEX_SPEC_IMAGE];
        }
        String num() {
            return strs[PARSE_INDEX_SPEC_NUM];
        }
        String allocOnce() {
            if(strs.length > PARSE_INDEX_SPEC_ALLOCONCE)
                return strs[PARSE_INDEX_SPEC_ALLOCONCE];
            return null;
        }
    }
    
    private static CloudConfiguration parseCloudSpecLine(String line, Map<String, ProviderCredential> providers)
    {
        if(line.trim().charAt(0) == '#')
            return null;
        // Line = "aws-ec2:t1.micro:ap_southeast-2a:ami-e2ba2cd8:1:1" - First deliminator is colon
        // Line = ".......:1,1024,8012:............:Ubuntu,12.04,64:." - Second deliminator is comma
        CloudSpecString str = new CloudSpecString(line);

        ProviderCredential provider = providers.get(str.provider());
        if(provider == null)
            throw new IllegalArgumentException();
        
        CloudConfiguration spec = new CloudConfiguration(provider);

        if(str.hardware().contains(","))
        {
            String[] hw = str.hardware().split(",");
            double cpu = Double.parseDouble(hw[0]);
            double ram = Double.parseDouble(hw[1]);
            double disk = Double.parseDouble(hw[2]);
            
            spec.setResource(new ResourceSpecDetail(cpu, ram, disk));
        }
        else
        {
            spec.setResource(new ResourceSpecId(str.hardware()));
        }
        
        spec.setLocation(new LocationSpecId(str.location()));
        
        if(str.image().contains(","))
        {
            String[] os = str.image().split(",");
            spec.setImage(new ImageSpecDetail(os[0], os[1], os[2]));
        }
        else
        {
            spec.setImage(new ImageSpecId(str.image()));
        }
        
        spec.setNumMachines(Integer.parseInt(str.num()));
        
        if("1".equals(str.allocOnce()))
            spec.setNeedAllocateOnce(true);
        else
            spec.setNeedAllocateOnce(false);
        
        return spec;
    }


    public static List<CloudConfiguration> parseCloudSpec(String filename, Map<String,ProviderCredential> providers)
    {
        List<CloudConfiguration> specs = new ArrayList<CloudConfiguration>();
        
        try {
            inputStream = new BufferedReader(new FileReader(filename));
            
            String line;
            while((line = inputStream.readLine())!= null)
            {
                CloudConfiguration spec = parseCloudSpecLine(line, providers);
                if(spec != null)
                    specs.add(spec);
            }
        } catch (IOException e) {
            System.err.println(e);
        }
        
        return specs;
    }
}
