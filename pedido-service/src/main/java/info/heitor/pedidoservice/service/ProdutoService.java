package info.heitor.pedidoservice.service;

import info.heitor.pedidoservice.model.Produto;
import info.heitor.pedidoservice.service.feign.ProdutoClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoClient produtoClient;

    public Produto findById(Long id) {
        return produtoClient.findById(id);
    }
}
