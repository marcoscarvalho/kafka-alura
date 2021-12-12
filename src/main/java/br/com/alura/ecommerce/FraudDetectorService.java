package br.com.alura.ecommerce;

import java.io.IOException;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public class FraudDetectorService {

	private void parse(ConsumerRecord<String, Order> record) {
		System.out.println("----------------------------------------");
		System.out.println("Processing new order, checking for fraud");
		System.out.println("key: " + record.key());
		System.out.println("value: " + record.value());
		System.out.println("partition: " + record.partition());
		System.out.println("offset: " + record.offset());

		try {
			Thread.sleep(Util.TEMPO_EXECUCAO);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println("Order processed");
	}

	public static void main(String[] args) throws IOException {

		var fraudDetectorService = new FraudDetectorService();
		try (var service = new KafkaService<>(FraudDetectorService.class.getSimpleName(), "ECOMMERCE_NEW_ORDER",
				fraudDetectorService::parse, Order.class, Map.of())) {
			service.run();
		}
	}

}
