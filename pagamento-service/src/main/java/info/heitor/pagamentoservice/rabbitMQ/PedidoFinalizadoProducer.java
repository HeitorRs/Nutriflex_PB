package info.heitor.pagamentoservice.rabbitMQ;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import info.heitor.pagamentoservice.model.Pedido;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PedidoFinalizadoProducer {
    private final AmqpTemplate amqpTemplate;
    private final ObjectMapper objectMapper;

    public void enviar(Pedido pedido) throws JsonProcessingException {
        amqpTemplate.convertAndSend("pedido-finalizado.exc","pedido-finalizado.rk", objectMapper.writeValueAsString(pedido));
    }
}
