package info.heitor.pagamentoservice.rabbitMQ;

import brave.Tracing;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import info.heitor.pagamentoservice.model.Pedido;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PagamentoProducer {

    private final AmqpTemplate amqpTemplate;
    private final ObjectMapper objectMapper;

    public void enviarFilaSucesso(Pedido pedido) throws JsonProcessingException {
        amqpTemplate.convertAndSend("pagamento-sucesso.exc","pagamento-sucesso.rk", objectMapper.writeValueAsString(pedido));
    }

    public void enviarFilaErro(Pedido pedido) throws JsonProcessingException {
        amqpTemplate.convertAndSend("pagamento-erro.exc", "pagamento-erro.rk", objectMapper.writeValueAsString(pedido));
    }

    public void enviarFilaPagamento(Pedido pedido) throws JsonProcessingException {
        amqpTemplate.convertAndSend("pagamento.exc","pagamento.rk", objectMapper.writeValueAsString(pedido));
    }
}
