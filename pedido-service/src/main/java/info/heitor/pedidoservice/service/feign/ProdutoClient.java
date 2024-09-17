package info.heitor.pedidoservice.service.feign;

import info.heitor.pedidoservice.model.Produto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("PRODUTO-SERVICE")
public interface ProdutoClient {
    @GetMapping("/{id}")
    Produto findById(@PathVariable Long id);
}
