package info.heitor.pedidoservice.service.feign;

import info.heitor.pedidoservice.model.DescontoResponsePayload;
import info.heitor.pedidoservice.model.Pedido;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("DESCONTO-SERVICE")
public interface DescontoClient {
    @PostMapping()
    DescontoResponsePayload calcularDesconto(@RequestBody Pedido pedido);
}
