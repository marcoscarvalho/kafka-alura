package br.com.alura.ecommerce;

import java.io.IOException;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public class EmailService {

	private void parse(ConsumerRecord<String, String> record) {
		System.out.println("----------------------------------------");
		System.out.println("Send Email");
		System.out.println("key: " + record.key());
		System.out.println("value: " + record.value());
		System.out.println("partition: " + record.partition());
		System.out.println("offset: " + record.offset());

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println("Email send");
	}

	public static void main(String[] args) throws IOException {
		var emailService = new EmailService();
		try (var service = new KafkaService(EmailService.class.getSimpleName(), "ECOMMERCE_SEND_EMAIL",
				emailService::parse)) {
			service.run();
		}
	}

}
