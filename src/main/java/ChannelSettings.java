import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class ChannelSettings {
    private String channelAd;
    private String biArticlesLink; // Ссылка на статьи о BI-системах

    // Приватный конструктор
    private ChannelSettings(String channelAd, String biArticlesLink) {
        this.channelAd = channelAd;
        this.biArticlesLink = biArticlesLink;
    }

    // Метод для загрузки настроек из файла
    public static ChannelSettings loadFromFile(String filePath) throws IOException {

        Properties properties = new Properties();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            properties.load(reader); // Загружаем свойства из файла
        }

        // Получаем значения из файла
        String channelAd = properties.getProperty("channel.ad");
        String biArticlesLink = properties.getProperty("bi.articles.link");

        // Проверяем, что все необходимые значения загружены
        if (channelAd == null || biArticlesLink == null) {
            throw new IOException("Не удалось загрузить настройки канала из файла.");
        }

        return new ChannelSettings(channelAd, biArticlesLink);
    }

    // Геттер для рекламы канала
    public String getChannelAd() {
        return channelAd;
    }

    // Геттер для ссылки на статьи о BI-системах
    public String getBiArticlesLink() {
        return biArticlesLink;
    }
}