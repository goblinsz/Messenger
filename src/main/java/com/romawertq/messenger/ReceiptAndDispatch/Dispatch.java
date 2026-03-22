package com.romawertq.messenger.ReceiptAndDispatch;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;

public class Dispatch {
    private static final String HOST_NAME = "*";
    private static final String EXCHANGE_NAME = "direct_exchange";

    public static class MessageData {
        public String from;
        public String content;
        public String timestamp;

        public MessageData(String from, String content) {
            this.from = from;
            this.content = content;
            this.timestamp = Instant.now().toString();
        }
    }

    public static CompletableFuture<Boolean> sendMessageAsync(String content) {
        return CompletableFuture.supplyAsync(() -> {
            Connection connection = null;
            Channel channel = null;

            try {
                ConnectionFactory factory = new ConnectionFactory();
                factory.setHost(HOST_NAME);

                connection = factory.newConnection();
                channel = connection.createChannel();

                channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT, true);

                MessageData message = new MessageData("ляля", content);
                ObjectMapper mapper = new ObjectMapper();
                String jsonBody = mapper.writeValueAsString(message);
                byte[] bodyBytes = jsonBody.getBytes(StandardCharsets.UTF_8);

                AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
                        .contentType("application/json")
                        .contentEncoding("UTF-8")
                        .build();

                channel.basicPublish(EXCHANGE_NAME, "ляля", properties, bodyBytes);

                System.out.println("✅ Отправлено: " + content);
                return true;

            } catch (Exception e) {
                System.err.println("❌ Ошибка: " + e.getMessage());
                return false;
            } finally {
                try {
                    if (channel != null) channel.close();
                    if (connection != null) connection.close();
                } catch (Exception e) { /* ignore */ }
            }
        });
    }
}