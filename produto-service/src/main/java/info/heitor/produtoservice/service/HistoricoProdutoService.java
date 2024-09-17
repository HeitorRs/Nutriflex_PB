package info.heitor.produtoservice.service;

import info.heitor.produtoservice.model.HistoricoProduto;
import info.heitor.produtoservice.repository.HistoricoProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoricoProdutoService implements IHistoricoProdutoService{

    private final HistoricoProdutoRepository historicoProdutoRepository;


    @Override
    public List<HistoricoProduto> buscarTodos() {
        return historicoProdutoRepository.findAll();
    }

    @Override
    public List<HistoricoProduto> buscarPorId(String codigoProduto) {
        return historicoProdutoRepository.findByCodigoProduto(codigoProduto);
    }
}
