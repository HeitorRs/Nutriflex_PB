package info.heitor.produtoservice.rabbitMQ;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import info.heitor.produtoservice.model.Pedido;
import info.heitor.produtoservice.service.ProdutoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EstoqueConsumer {

    private final ProdutoService produtoService;

    @RabbitListener(queues = {"estoque"})
    public void recebe(@Payload String menssagem) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Pedido pedido = objectMapper.readValue(menssagem, Pedido.class);
        produtoService.atualizarEstoque(pedido);
        log.info("Estoque atualizado com sucesso para o pedido: {}", pedido);
    }
}
