import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class BotSettings {
    private final Properties props = new Properties();

    public BotSettings() throws IOException {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("botsettings.txt")) {
            props.load(input);
        }  catch (IOException e) {
        e.printStackTrace();
    }
    }

    public String getBotUsername() { return props.getProperty("botUsername"); }
    public String getBotToken() { return props.getProperty("botToken"); }
    public String getOwnerName() { return props.getProperty("ownerName"); }
    public String getOwnerAccount() { return props.getProperty("ownerAccount"); }
    public String getOwnerWebsite() { return props.getProperty("ownerWebsite"); }
    public String getOwnerGitHub() { return props.getProperty("ownerGitHub"); }
}
