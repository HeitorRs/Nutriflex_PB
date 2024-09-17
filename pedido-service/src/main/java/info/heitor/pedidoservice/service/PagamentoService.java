package info.heitor.pedidoservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import info.heitor.pedidoservice.model.Pedido;
import info.heitor.pedidoservice.model.PedidoStatus;
import info.heitor.pedidoservice.rabbitMQ.PagamentoProducer;
import info.heitor.pedidoservice.repository.PedidoRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PagamentoService {
    private final PagamentoProducer pagamentoProducer;
    private final PedidoRepository pedidoRepository;

    public Pedido processarPagamento(Pedido pedido) {
        try {
            pagamentoProducer.enviar(pedido);
            pedido.setStatus(PedidoStatus.PROCESSANDO_PAGAMENTO);
        }catch (JsonProcessingException e) {
            pedido.setStatus(PedidoStatus.ERRO);
        }
        return pedidoRepository.save(pedido);
    }
}
