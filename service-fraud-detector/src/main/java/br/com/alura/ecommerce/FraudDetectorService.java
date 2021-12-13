package br.com.alura.ecommerce;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public class FraudDetectorService {
	
	private final KafkaDispatcher<Order> orderDispatcher = new KafkaDispatcher<Order>();

	private void parse(ConsumerRecord<String, Order> record) throws InterruptedException, ExecutionException {
		System.out.println("----------------------------------------");
		System.out.println("Processing new order, checking for fraud");
		System.out.println("key: " + record.key());
		System.out.println("value: " + record.value());
		System.out.println("partition: " + record.partition());
		System.out.println("offset: " + record.offset());

		try {
			Thread.sleep(Util.TEMPO_EXECUCAO_RAPIDO);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		if(isFraud(record) ) {
			//pretending that the fraud happens when the amount is >= 4000
			System.out.println("Order is a Fraud!!!!!!!!");
			orderDispatcher.send("ECOMMERCE_ORDER_REJECTED", record.value().getEmail(), record.value());
		} else {
			System.out.println("Approved " + record.value());
			orderDispatcher.send("ECOMMERCE_ORDER_APPROVED", record.value().getEmail(), record.value());
		}
		
		System.out.println("Order processed");
	}

	private boolean isFraud(ConsumerRecord<String, Order> record) {
		return record.value().getAmount().compareTo(new BigDecimal("4000")) >= 0;
	}

	public static void main(String[] args) throws IOException {

		var fraudDetectorService = new FraudDetectorService();
		try (var service = new KafkaService<>(FraudDetectorService.class.getSimpleName(), "ECOMMERCE_NEW_ORDER",
				fraudDetectorService::parse, Order.class, Map.of())) {
			service.run();
		}
	}

}
