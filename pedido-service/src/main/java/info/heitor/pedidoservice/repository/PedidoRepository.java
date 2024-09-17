package info.heitor.pedidoservice.repository;

import info.heitor.pedidoservice.model.Pedido;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PedidoRepository extends MongoRepository<Pedido, String> {
}
