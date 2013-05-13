package provider;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ProviderCredentialParser {

    private final static int PARSE_INDEX_PROVIDER_ID = 1;
    private final static int PARSE_INDEX_PROVIDER_PASSWORD = 2;
    private final static int PARSE_INDEX_PROVIDER_PROVIDER = 0;
    
    private static BufferedReader inputStream;

    public static Map<String, ProviderCredential> parseProvider(String filename)
    {
        Map<String, ProviderCredential> providers = new HashMap<String, ProviderCredential>();
        
        try {
            inputStream = new BufferedReader(new FileReader(filename));
            
            String line;
            while((line = inputStream.readLine())!= null)
            {
                ProviderCredential spec = parseProviderLine(line);
                providers.put(spec.getProvider(), spec);
            }
        } catch (IOException e) {
            System.err.println(e);
        }
        
        return providers;
    }

    private static ProviderCredential parseProviderLine(String line)
    {
        // Line = "aws-ec2:USERPHRASE:PASSWORDPHARSE" - deliminator is colon.
        String [] str = line.trim().split(":");
        
        ProviderCredential spec = new ProviderCredential(str[PARSE_INDEX_PROVIDER_PROVIDER],
                str[PARSE_INDEX_PROVIDER_ID], str[PARSE_INDEX_PROVIDER_PASSWORD]);
    
        return spec;
    }

}
