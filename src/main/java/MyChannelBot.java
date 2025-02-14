import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ForceReplyKeyboard;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

//import org.telegram.telegrambots.meta.api.objects.*;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MyChannelBot extends TelegramLongPollingBot {
    private final BotSettings botSettings;
    private final AdSettings adSettings;

    public MyChannelBot() throws Exception {
        this.botSettings = new BotSettings();
        this.adSettings = new AdSettings();
    }

    @Override
    public String getBotUsername() {
        return botSettings.getBotUsername();
    }

    @Override
    public String getBotToken() {
        return botSettings.getBotToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasCallbackQuery()) {
            onCallbackQueryReceived(update.getCallbackQuery());
        } else if (update.hasMessage() && update.getMessage().hasText()) {
            Message message = update.getMessage();
            String text = message.getText();
            long userId = message.getFrom().getId();
            String username = message.getFrom().getUserName();
            Long chatId = message.getChatId();

            try {
                switch (text) {
                    case "/start":
                        sendMainMenu(chatId);
                        break;
                    case "ℹ️ Информация":
                        sendBotInfo(chatId);
                        break;
                    case "🌐 Сайт":
                        sendWebsiteLink(chatId);
                        break;
                    case "📢 Реклама":
                        sendAdMaterials(chatId);
                        break;
                    case "📝 Запись":
                        requestConsultationDate(chatId);
                        break;
                    case "📋 Мои записи":
                        sendUserBookings(userId, chatId);
                        break;
                    default:
                        processDateInput(text, userId, chatId, username);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private ReplyKeyboardMarkup createMainMenuKeyboard() {
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        markup.setResizeKeyboard(true);
        markup.setOneTimeKeyboard(false);

        List<KeyboardRow> rows = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        row1.add("ℹ️ Информация");
        row1.add("🌐 Сайт");

        KeyboardRow row2 = new KeyboardRow();
        row2.add("📢 Реклама");
        row2.add("📝 Запись");

        KeyboardRow row3 = new KeyboardRow();
        row3.add("📋 Мои записи");

        rows.add(row1);
        rows.add(row2);
        rows.add(row3);

        markup.setKeyboard(rows);
        return markup;
    }

    private void sendMainMenu(Long chatId) throws TelegramApiException {
        SendMessage msg = new SendMessage();
        msg.setChatId(chatId.toString());
        msg.setText("Добро пожаловать! Выберите действие:");
        msg.setReplyMarkup(createMainMenuKeyboard());

        // Логируем отправку сообщения
        System.out.println("Отправка главного меню в чат: " + chatId);

        execute(msg);
    }

    private void sendBotInfo(Long chatId) throws TelegramApiException {
        String info = "👨💻 Владелец бота:\n"
                + "• Имя: " + botSettings.getOwnerName() + "\n"
                + "• Telegram: " + botSettings.getOwnerAccount() + "\n"
                + "• GitHub: " + botSettings.getOwnerGitHub() + "\n"
                + "• BI статьи: " + adSettings.getBiArticlesLink();
        execute(new SendMessage(chatId.toString(), info));
    }

    private void sendWebsiteLink(Long chatId) throws TelegramApiException {
        execute(new SendMessage(chatId.toString(), "🌐 Сайт: " + botSettings.getOwnerWebsite()));
    }

    private void sendAdMaterials(Long chatId) throws TelegramApiException {
        execute(new SendMessage(chatId.toString(), adSettings.getChannelAdText()));
    }

    private void requestConsultationDate(Long chatId) throws TelegramApiException {
       /*   SendMessage msg = new SendMessage();
        msg.setChatId(chatId.toString());
        msg.setText("Введите дату в формате ДД.ММ.ГГГГ ЧЧ:ММ\nПример: 25.12.2024 14:30");
        msg.setReplyMarkup(new ForceReplyKeyboard());
        execute(msg);*/


      //  Изменение метода requestConsultationDate
        // Этот метод отвечает за отправку клавиатуры с выбором даты.


     //   private void requestConsultationDate(Long chatId) throws TelegramApiException {
        SendMessage msg = new SendMessage();
        msg.setChatId(chatId.toString());
        msg.setText("Выберите дату консультации:");

        // Создаем клавиатуру с датами
         InlineKeyboardMarkup button = new InlineKeyboardMarkup();
       /*button.setText(formattedDate);
        button.setCallbackData(formattedDate); // Устанавливаем callback_data
*/
        List<List<InlineKeyboardButton>> keyboardRows = new ArrayList<>();

        // Добавляем кнопки с датами (например, текущие три дня)
        for (int i = 0; i < 3; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, i); // Текущий день + i дней
            Date date = calendar.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            String formattedDate = sdf.format(date);

            // InlineKeyboardButton button = new InlineKeyboardButton(formattedDate, formattedDate);
            InlineKeyboardButton button1 = new InlineKeyboardButton(); //(formattedDate, null, formattedDate, null, null, null, null, null, null);
            button1.setText(formattedDate);
            button1.setCallbackData(formattedDate); // Устанавливаем callback_data

            List<InlineKeyboardButton> row = new ArrayList<>();
            row.add(button1);
            keyboardRows.add(row);
        }

        button.setKeyboard(keyboardRows);
        msg.setReplyMarkup(button);

        execute(msg);
        }

    private void processDateInput(String text, long userId, Long chatId, String username) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        sdf.setLenient(false);
        System.out.println(userId);
        try {
            Date parsedDate = sdf.parse(text);
            String formattedDate = sdf.format(parsedDate);
            DatabaseManager.addConsultation(userId, username, formattedDate);
            execute(new SendMessage(chatId.toString(), "✅ Запись на " + formattedDate + " успешно добавлена"));
            System.out.println(userId);
        } catch (ParseException e) {
            sendErrorMessage(chatId, "❌ Неверный формат! Используйте ДД.ММ.ГГГГ ЧЧ:ММ");
            System.out.println(userId);
        } catch (Exception e) {
            sendErrorMessage(chatId, "⚠️ Ошибка при сохранении записи");
            System.out.println(userId);

        }
    }

    private void sendUserBookings(long userId, Long chatId) throws SQLException, TelegramApiException {
        try {
            String bookings = DatabaseManager.getUserConsultations(userId);
            if (bookings == null || bookings.isEmpty()) {
                sendErrorMessage(chatId, "⚠️ У вас нет записей.");
                return;
            }
            execute(new SendMessage(chatId.toString(), bookings));
        } catch (SQLException e) {
            e.printStackTrace();
            sendErrorMessage(chatId, "⚠️ Ошибка базы данных: " + e.getMessage());
        } catch (TelegramApiException e) {
            e.printStackTrace();
            sendErrorMessage(chatId, "⚠️ Ошибка при отправке сообщения: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            sendErrorMessage(chatId, "⚠️ Неизвестная ошибка: " + e.getMessage());
        }
    }


    private void sendErrorMessage(Long chatId, String text) {
        try {
            execute(new SendMessage(chatId.toString(), text));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


  /*  @Override
    public void onCallbackQueryReceived(CallbackQuery callbackQuery) {
        String data = callbackQuery.getData(); // Получаем выбранную дату
        long userId = callbackQuery.getFrom().getId();
        Long chatId = callbackQuery.getMessage().getChatId();

        if (data.matches("\\d{2}.\\d{2}.\\d{4}")) { // Проверяем, что это дата
            showAvailableTimeslots(data, userId, chatId);
        }
    }*/

   // Этот метод обрабатывает выбор пользователем даты и времени.

    public void onCallbackQueryReceived(CallbackQuery callbackQuery) {
        String data = callbackQuery.getData(); // Получаем выбранную дату или дату и время
        long userId = callbackQuery.getFrom().getId();
        Long chatId = callbackQuery.getMessage().getChatId();
        String username = callbackQuery.getFrom().getUserName();

        System.out.println(data);
        if (data.matches("\\d{2}.\\d{2}.\\d{4}")) { // Проверяем, что это дата
            showAvailableTimeslots(data, userId, chatId);
        } else if (data.contains(" ")) { // Проверяем, что это дата и время
            String[] parts = data.split(" ");
            String selectedDate = parts[0];
            String selectedTime = parts[1];

            saveBooking(selectedDate, selectedTime, userId, chatId);
        } else {
            sendErrorMessage(chatId, "⚠️ Неверный формат данных");
        }

    }

   // Обработка нажатия на кнопку с датой
   // Когда пользователь выберет дату, бот должен отправить ему доступные временные интервалы. Для этого обработаем callback запросы.


    //Этот метод отвечает за отображение доступных временных интервалов после выбора даты.

    private void showAvailableTimeslots(String selectedDate, long userId, Long chatId) {
        SendMessage msg = new SendMessage();
        msg.setChatId(chatId.toString());
        msg.setText("Выберите время консультации:");

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboardRows = new ArrayList<>();

        for (int hour = 8; hour <= 18; hour++) {
            String timeSlot = String.format("%02d:00-%02d:59", hour, hour + 1);
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(timeSlot);
            button.setCallbackData(selectedDate + " " + timeSlot);

            List<InlineKeyboardButton> row = new ArrayList<>();
            row.add(button);
            keyboardRows.add(row);
        }

        inlineKeyboardMarkup.setKeyboard(keyboardRows);
        msg.setReplyMarkup(inlineKeyboardMarkup);

        try {
            execute(msg);
        } catch (TelegramApiException e) {
            e.printStackTrace();
            sendErrorMessage(chatId, "⚠️ Ошибка при отправке временных интервалов: " + e.getMessage());
        }
    }


    //Этот метод сохраняет запись о консультации в базу данных.
private void saveBooking(String selectedDate, String selectedTime, long userId, Long chatId) {
        String fullDateTime = selectedDate + " " + selectedTime.substring(0, 5); // Форматируем дату и время
        try {
            DatabaseManager.addConsultation(userId, "", fullDateTime);
            execute(new SendMessage(chatId.toString(), "✅ Запись на " + fullDateTime + " успешно добавлена"));
        } catch (Exception e) {
            sendErrorMessage(chatId, "⚠️ Ошибка при сохранении записи");
        }
    }
}
