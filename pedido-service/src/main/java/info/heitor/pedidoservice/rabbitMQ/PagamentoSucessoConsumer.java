package info.heitor.pedidoservice.rabbitMQ;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import info.heitor.pedidoservice.model.Pedido;
import info.heitor.pedidoservice.model.PedidoStatus;
import info.heitor.pedidoservice.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PagamentoSucessoConsumer {

    private final PedidoRepository pedidoRepository;
    private final TransportadoraProducer transportadoraProducer;

    @RabbitListener(queues = {"pagamento-sucesso"})
    public void recebe(@Payload String menssagem) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Pedido pedido = objectMapper.readValue(menssagem, Pedido.class);
        log.info("Pagamento finalizado: {}",pedido);
        pedido.setStatus(PedidoStatus.NA_TRANSPORTADORA);
        pedidoRepository.save(pedido);
        transportadoraProducer.enviar(pedido);
    }
}
