package br.com.alura.ecommerce;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public class CreateUserService {

	private Connection connection;

	CreateUserService() throws SQLException {
		String url = "jdbc:sqlite:target/users_database.db";
		this.connection = DriverManager.getConnection(url);
		connection.createStatement().execute("create table Users (uuid varchar(200) primary key, email varchar(200)) ");
	}

	private void parse(ConsumerRecord<String, Order> record) throws Exception {
		System.out.println("----------------------------------------");
		System.out.println("Processing new order, checking for new user");
		System.out.println("value: " + record.value());

		try {
			Thread.sleep(Util.TEMPO_EXECUCAO_RAPIDO);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		if (isNewUser(record.value().getEmail())) {
			insertNewUser(record.value());
		}
	}

	private void insertNewUser(Order order) throws SQLException {
		var insert = connection.prepareStatement("insert into Users (uuid, email) values (?, ?) ");
		insert.setString(1, UUID.randomUUID().toString());
		insert.setString(2, order.getEmail());
		insert.execute();
		System.out.println("email adicionado: " + order.getEmail());
	}

	private boolean isNewUser(String email) throws SQLException {
		var exists = connection.prepareStatement("select uuid from Users where email = ? limit 1");
		exists.setString(1, email);
		var results = exists.executeQuery();
		return !results.next();
	}

	public static void main(String[] args) throws IOException, SQLException {

		var fraudDetectorService = new CreateUserService();
		try (var service = new KafkaService<>(CreateUserService.class.getSimpleName(), "ECOMMERCE_NEW_ORDER",
				fraudDetectorService::parse, Order.class, Map.of())) {
			service.run();
		}
	}

}
