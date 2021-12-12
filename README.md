# kafka-alura

#### iniciar zookeeper
bin/zookeeper-server-start.sh config/zookeeper.properties

#### iniciar kafka
bin/kafka-server-start.sh config/server.properties

#### criar topic
bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic LOJA_NOVO_PEDIDO

#### listar topic
bin/kafka-topics.sh --list --bootstrap-server localhost:9092

#### descrever tópico
bin/kafka-topics.sh --bootstrap-server localhost:9092 --describe

#### escutar tópico desde início
bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic ECOMMERCE_NEW_ORDER --from-beginning

#### postar tópic
bin/kafka-console-producer.sh --broker-list localhost:9092 --topic LOJA_NOVO_PEDIDO

#### Particionar tópico em mais partições para conseguir ter mais de um consumo.
bin/kafka-topics.sh --alter --bootstrap-server localhost:9092 --topic ECOMMERCE_NEW_ORDER --partitions 3

#### Descrever todos os consumer groups
bin/kafka-consumer-groups.sh --bootstrap-server localhost:9092 --describe --all-groups