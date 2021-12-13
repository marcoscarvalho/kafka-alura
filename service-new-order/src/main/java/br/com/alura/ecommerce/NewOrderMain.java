package br.com.alura.ecommerce;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewOrderMain {

	public static void main(String[] args) throws InterruptedException, ExecutionException, IOException {
		try (var orderDispatcher = new KafkaDispatcher<Order>()) {
			try (var emailDispatcher = new KafkaDispatcher<Email>()) {

				//var userId = UUID.randomUUID().toString();
				var email = UUID.randomUUID().toString() + "@gmail.com";

				for (int i = 0; i < 100; i++) {
					var orderId = UUID.randomUUID().toString();
					var amount = new BigDecimal(Math.random() * 5000 + 1);
					var order = new Order(orderId, amount, email);
					orderDispatcher.send("ECOMMERCE_NEW_ORDER", email, order);

					var valueSubject = "Order ok";
					var valueEmail = "Thank you for your order!";
					emailDispatcher.send("ECOMMERCE_SEND_EMAIL", email, new Email(email, valueSubject, valueEmail));
				}

			}
		}

	}

}
