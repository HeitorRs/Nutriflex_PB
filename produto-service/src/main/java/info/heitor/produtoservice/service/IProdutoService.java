package info.heitor.produtoservice.service;


import info.heitor.produtoservice.filters.ProdutoFiltros;
import info.heitor.produtoservice.model.Produto;

import java.util.List;
import java.util.Optional;

public interface IProdutoService {

    public List<Produto> listarTodosProdutos();

    public Produto salvarProduto(Produto produto);

    public Optional<Produto> encontrarProdutoPorId(Long id);

    public void deletarProdutoPorId(Long id);

    public Produto atualizarProduto(Long id, Produto produtoAtualizado);

    List<Produto> buscarFiltros(ProdutoFiltros filtros) ;
}
