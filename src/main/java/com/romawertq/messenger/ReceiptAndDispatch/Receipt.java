package com.romawertq.messenger.ReceiptAndDispatch;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

public class Receipt {
    // Настройки
    private static final String HOST = "localhost";
    private static final String EXCHANGE_NAME = "direct_exchange";

    // ЭТОТ ID ДОЛЖЕН СОВПАДАТЬ С ТЕМ, ЧТО ВВЕДЕТ PYTHON ОТПРАВИТЕЛЬ
    private static final String MY_CLIENT_ID = "java_client";

    public static void ff() throws Exception {
        // 1. Настройка фабрики подключений
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOST);

        System.out.println("--- ПОЛУЧАТЕЛЬ (Java) запущен ---");
        System.out.println("Ожидание сообщений для ID: " + MY_CLIENT_ID);

        // 2. Подключение
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        // 3. Объявляем тот же Exchange
        channel.exchangeDeclare(EXCHANGE_NAME, "direct", true);

        // 4. Создаем очередь с именем нашего ID
        // durable=true, чтобы очередь не пропала
        String queueName = MY_CLIENT_ID;
        channel.queueDeclare(queueName, true, false, false, null);

        // 5. Привязываем очередь к Exchange по ключу (MY_CLIENT_ID)
        // Теперь все сообщения с routing_key = "java_client" полетят сюда
        channel.queueBind(queueName, EXCHANGE_NAME, MY_CLIENT_ID);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String messageJson = new String(delivery.getBody(), StandardCharsets.UTF_8);

            JSONObject json = new JSONObject(messageJson);

            String content = json.getString("content");

            System.out.println("🥔 Чистый контент: " + content);
            System.out.println("----------------------------");

            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        };

        // 7. Запускаем потребление (autoAck=false для надежности)
        channel.basicConsume(queueName, false, deliverCallback, consumerTag -> {});

        // Держим программу запущенной, чтобы она слушала
        System.out.println("Нажмите Ctrl+C для выхода...");
        System.in.read();

        connection.close();
    }
}