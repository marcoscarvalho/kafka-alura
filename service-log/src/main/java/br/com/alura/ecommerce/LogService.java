package br.com.alura.ecommerce;

import java.io.IOException;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;

public class LogService {

	private void parse(ConsumerRecord<String, String> record) {
		System.out.println("----------------------------------------");
		System.out.println("LOG: " + record.topic());
		System.out.println("key: " + record.key());
		System.out.println("value: " + record.value());
		System.out.println("partition: " + record.partition());
		System.out.println("offset: " + record.offset());

		try {
			Thread.sleep(Util.TEMPO_EXECUCAO_RAPIDO);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException {

		var logService = new LogService();
		try (var service = new KafkaService(LogService.class.getSimpleName(), Pattern.compile("ECOMMERCE_.*"),
				logService::parse, String.class,
				Map.of(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class))) {
			service.run();
		}
	}

}
