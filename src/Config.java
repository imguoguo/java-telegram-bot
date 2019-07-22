import java.io.*;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.nio.file.Files;
import java.util.Properties;

public class Config {
    final Properties properties = new Properties();;

    Config(String FileName) throws IOException {
        File configFile = new File(FileName);
        if (configFile.exists()){
            InputStream is = new BufferedInputStream(new FileInputStream(configFile));
            properties.load(is);
        } else {
            File exampleConfigFile = new File("/prop.example.properties");
            if (!exampleConfigFile.exists()){
                exampleConfigFile = new File("prop.example.properties");
            }
            Files.copy(exampleConfigFile.toPath(), configFile.toPath());
            throw new FileNotFoundException();
        }
    }
    public String getBotToken(){
        return properties.getProperty("TGBOT_TOKEN");
    }
    public String getUsername(){
        return properties.getProperty("TGBOT_USERNAME");
    }
    public Proxy getProxy() {
        Proxy proxy;
        if(Boolean.parseBoolean(properties.getProperty("PROXY_ON"))) {
            Proxy.Type type = Proxy.Type.DIRECT;
            String addr = properties.getProperty("PROXY_ADDR");
            int port = Integer.parseInt(properties.getProperty("PROXY_PORT"));
            switch(properties.getProperty("PROXY_TYPE")){
                case "HTTP":
                    type = Proxy.Type.HTTP;
                    break;
                case "SOCKS":
                    type = Proxy.Type.SOCKS;
                    break;
            }
            proxy = new Proxy(type, new InetSocketAddress(addr, port));
        } else {
            proxy = Proxy.NO_PROXY;
        }
        return proxy;
    }

}
