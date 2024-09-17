package info.heitor.pagamentoservice.rabbitMQ;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import info.heitor.pagamentoservice.model.Pedido;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TransportadoraConsumer {

    private final PedidoFinalizadoProducer pedidoFinalizadoProducer;

    @RabbitListener(queues = {"transportadora"})
    public void recebe(@Payload String mensagem) throws JsonProcessingException {
        log.info("Pedido na Transportadora: {}", mensagem);
        ObjectMapper objectMapper = new ObjectMapper();
        Pedido pedido = objectMapper.readValue(mensagem, Pedido.class);
        try {
            Thread.sleep(3_000);
            pedidoFinalizadoProducer.enviar(pedido);
        }catch (InterruptedException e){
            throw new RuntimeException(e);
        }

    }
}
