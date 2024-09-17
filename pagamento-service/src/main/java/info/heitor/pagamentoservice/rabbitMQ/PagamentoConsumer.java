package info.heitor.pagamentoservice.rabbitMQ;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import info.heitor.pagamentoservice.model.Pedido;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@RequiredArgsConstructor
@Slf4j
public class PagamentoConsumer {
    private final PagamentoProducer pagamentoProducer;

    @RabbitListener(queues = {"pagamento"})
    public void receber(@Payload String mensagem) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        Pedido pedido = objectMapper.readValue(mensagem, Pedido.class);
        if (pedido.getSerial() < 1500){
            log.info("Pagamento rápido: {}" , mensagem);
            try {
                Thread.sleep(4_000);
                pagamentoProducer.enviarFilaSucesso(pedido);
            }catch (InterruptedException e){
                throw new RuntimeException(e);
            }
        }else if (pedido.getSerial() >= 1500 && pedido.getSerial() <= 2000){
            log.info("Pagamento lento: {}",mensagem);
            try {
                Thread.sleep(10_000);
                pagamentoProducer.enviarFilaSucesso(pedido);
            }catch (InterruptedException e){
                throw new RuntimeException(e);
            }
        }else if (pedido.getSerial() > 2000){
            log.info("Pagamento erro: {}",mensagem);
            pagamentoProducer.enviarFilaErro(pedido);
        }
    }

    @RabbitListener(queues = {"pagamento-erro"})
    public void recebeuErro(@Payload String mensagem) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Pedido pedido = objectMapper.readValue(mensagem, Pedido.class);
        long novoSerial = new Random().nextLong(3000);
        log.info("pagamento erro, gerando um novo Serial: {}", novoSerial);
        pedido.setSerial(novoSerial);
        pagamentoProducer.enviarFilaPagamento(pedido);
    }
}
