package br.com.alura.ecommerce;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewOrderMain {

	public static void main(String[] args) throws InterruptedException, ExecutionException, IOException {
		try (var dispatcher = new KafkaDispatcher()) {
			for (int i = 0; i < 100; i++) {
				var key = UUID.randomUUID().toString();
				var value = key + ",idUsuario,valorCompras";
				dispatcher.send("ECOMMERCE_NEW_ORDER", key, value);

				var valueEmail = "Thank you for your order!";
				dispatcher.send("ECOMMERCE_SEND_EMAIL", key, valueEmail);
			}

		}

	}

}
