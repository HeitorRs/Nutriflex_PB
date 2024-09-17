package info.heitor.pedidoservice.service;

import info.heitor.pedidoservice.model.DescontoResponsePayload;
import info.heitor.pedidoservice.model.Pedido;
import info.heitor.pedidoservice.service.feign.DescontoClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DescontoService {

    private final DescontoClient descontoClient;

    public DescontoResponsePayload getTotalDesconto(Pedido pedido) {
        return descontoClient.calcularDesconto(pedido);
    }
}
