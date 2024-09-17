package info.heitor.pedidoservice.rabbitMQ;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import info.heitor.pedidoservice.model.Pedido;
import info.heitor.pedidoservice.model.PedidoStatus;
import info.heitor.pedidoservice.repository.PedidoRepository;
import info.heitor.pedidoservice.service.PedidoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PedidoFinalizadoConsumer {
    private final PedidoRepository pedidoRepository;

    @RabbitListener(queues = {"pedido-finalizado"})
    public void recebe(@Payload String mensagem) throws JsonProcessingException {
        try {
            Thread.sleep(2_000);
        }catch (InterruptedException e){
            throw new RuntimeException(e);
        }
        ObjectMapper objectMapper = new ObjectMapper();
        Pedido pedido = objectMapper.readValue(mensagem, Pedido.class);
        pedido.setStatus(PedidoStatus.ENTREGE);
        pedidoRepository.save(pedido);
        log.info("Pedido finalizado: {}",mensagem);
    }
}
