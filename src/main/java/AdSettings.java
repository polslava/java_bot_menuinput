import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AdSettings {
    private final Properties props = new Properties();

    public AdSettings() throws IOException {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("adsettings.txt")) {
            props.load(input);
        }
    }

    public String getChannelAdText() { return props.getProperty("channelAd"); }
    public String getBiArticlesLink() { return props.getProperty("bi.articles.link"); }
}
