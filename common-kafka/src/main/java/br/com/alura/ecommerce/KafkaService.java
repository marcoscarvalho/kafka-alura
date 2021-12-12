package br.com.alura.ecommerce;

import java.io.Closeable;
import java.io.IOException;
import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.regex.Pattern;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

class KafkaService<T> implements Closeable {

	private final KafkaConsumer<String, T> consumer;
	private final ConsumerFunction<T> parse;

	KafkaService(String groupId, ConsumerFunction<T> parse, Class<T> type, Map<String, String> properties) {
		this.parse = parse;
		this.consumer = new KafkaConsumer<>(getProperties(type, properties, groupId));
	}

	KafkaService(String groupId, Pattern topic, ConsumerFunction<T> parse, Class<T> type,
			Map<String, String> properties) {
		this(groupId, parse, type, properties);
		consumer.subscribe(topic);
	}


	KafkaService(String groupId, String topic, ConsumerFunction<T> parse, Class<T> type,
			Map<String, String> properties) {
		this(groupId, parse, type, properties);
		consumer.subscribe(Collections.singletonList(topic));
	}


	private Properties getProperties(Class<T> type, Map<String, String> overrideProperties, String groupId) {
		var properties = new Properties();
		properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
		properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, GsonDeserializer.class.getName());
		properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
		properties.setProperty(ConsumerConfig.CLIENT_ID_CONFIG, UUID.randomUUID().toString());
		properties.setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "1");
		properties.setProperty(GsonDeserializer.TYPE_CONFIG, type.getName());
		properties.putAll(overrideProperties);
		return properties;
	}

	public void run() {
		while (true) {
			var records = consumer.poll(Duration.ofMillis(100));

			if (records.isEmpty()) {
				continue;
			}

			for (var record : records) {
				parse.consume(record);
			}
		}
	}

	@Override
	public void close() throws IOException {
		consumer.close();
	}

}
