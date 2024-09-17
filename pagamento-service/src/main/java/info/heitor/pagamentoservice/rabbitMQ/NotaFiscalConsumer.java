package info.heitor.pagamentoservice.rabbitMQ;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotaFiscalConsumer {

    @RabbitListener(queues = {"nota-fiscal"})
    public void recebe(@Payload String mensagem) throws InterruptedException {
        log.info("Enviando Nota fiscal: {}", mensagem);
        Thread.sleep(10_000);
    }
}
