package info.heitor.pedidoservice.rabbitMQ;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import info.heitor.pedidoservice.model.Pedido;
import lombok.AllArgsConstructor;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class PagamentoProducer {
    private final AmqpTemplate amqpTemplate;
    private final ObjectMapper objectMapper;

    public void enviar(Pedido pedido) throws JsonProcessingException {
        amqpTemplate.convertAndSend("pagamento.exc","pagamento.rk", objectMapper.writeValueAsString(pedido));
    }
}
