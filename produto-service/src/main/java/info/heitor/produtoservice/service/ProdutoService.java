package info.heitor.produtoservice.service;


import info.heitor.produtoservice.filters.ProdutoFiltros;
import info.heitor.produtoservice.model.AcaoHistorico;
import info.heitor.produtoservice.model.HistoricoProduto;
import info.heitor.produtoservice.model.Pedido;
import info.heitor.produtoservice.model.Produto;
import info.heitor.produtoservice.repository.HistoricoProdutoRepository;
import info.heitor.produtoservice.repository.ProdutoRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProdutoService implements IProdutoService {

    private final ProdutoRepository produtoRepository;
    private final HistoricoProdutoRepository historicoProdutoRepository;
    private final EntityManager entityManager;

    public List<Produto> buscarFiltros(ProdutoFiltros filtros) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Produto> cq = cb.createQuery(Produto.class);
        Root<Produto> produto = cq.from(Produto.class);
        List<Predicate> predicates = new ArrayList<>();

        if (!filtros.getNome().isEmpty()) {
            String query = "%" + filtros.getNome() + "%";
            Predicate nome = cb.like(cb.lower(produto.get("nome")), query.toLowerCase());
            predicates.add(nome);
        }

        if (filtros.getCategoria() != null) {
            Predicate categoria = cb.equal(produto.get("categoria"), filtros.getCategoria());
            predicates.add(categoria);
        }

        Predicate[] array = predicates.toArray(Predicate[]::new);
        cq.where(array);
        List<Produto> resultList = entityManager.createQuery(cq).getResultList();
        return resultList;
    }

    @Override
    public List<Produto> listarTodosProdutos() {
        return produtoRepository.findAll();
    }

    @Override
    @Transactional
    public Produto salvarProduto(Produto produto) {
        validarProduto(produto);
        produto.prePersist();
        Produto produtoSalvo = produtoRepository.save(produto);
        registrarHistorico(produtoSalvo.getCodigo(), AcaoHistorico.CRIACAO);
        return produtoSalvo;
    }


    @Override
    @Transactional
    public Optional<Produto> encontrarProdutoPorId(Long id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID inválido!");
        }
        return Optional.ofNullable(produtoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Produto não encontrado para o ID: " + id)));
    }

    @Override
    @Transactional
    public void deletarProdutoPorId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID inválido!");
        }
        Produto produto = produtoRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Produto não encontrado"));

        registrarHistorico(produto.getCodigo(),AcaoHistorico.EXCLUSAO);

        produtoRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Produto atualizarProduto(Long id, Produto produtoAtualizado) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID inválido!");
        }
        validarProduto(produtoAtualizado);
        Produto produtoExistente = produtoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Produto de ID: " + id + " não encontrado"));

        produtoExistente.setNome(produtoAtualizado.getNome());
        produtoExistente.setDescricao(produtoAtualizado.getDescricao());
        produtoExistente.setPreco(produtoAtualizado.getPreco());
        produtoExistente.setEstoque(produtoAtualizado.getEstoque());
        produtoExistente.setCategoria(produtoAtualizado.getCategoria());

        Produto produtoAtualizadoSalvo = produtoRepository.save(produtoExistente);
        registrarHistorico(produtoAtualizadoSalvo.getCodigo(), AcaoHistorico.ATUALIZACAO);

        return produtoAtualizadoSalvo;
    }

    private void validarProduto(Produto produto) {
        if (produto.getNome() == null || produto.getNome().isEmpty()) {
            throw new IllegalArgumentException("Nome do produto não pode estar em branco");
        }
        if (produto.getPreco() == null || produto.getPreco().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Preço do produto deve ser maior que zero");
        }
        if (produto.getEstoque() < 0) {
            throw new IllegalArgumentException("Estoque do produto não pode ser negativo");
        }
    }

    private void registrarHistorico(String codigoProduto, AcaoHistorico acao) {
        HistoricoProduto historico = new HistoricoProduto();
        historico.setCodigoProduto(codigoProduto);
        historico.setAcao(acao);
        historico.setDataHora(LocalDateTime.now());
        historicoProdutoRepository.save(historico);
    }

    public void atualizarEstoque(Pedido pedido) {
        pedido.getItens().forEach(itemPedido -> {
            Long produtoId = itemPedido.getProdutoId();
            int quantidade = itemPedido.getQuantidade();

            Produto produto = produtoRepository.findById(produtoId)
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado: " + produtoId));

            produto.setEstoque(produto.getEstoque() - quantidade);
            produtoRepository.save(produto);  // Salva a atualização no banco de dados H2

            log.info("Estoque do produto {} atualizado. Quantidade removida: {}", produto.getNome(), quantidade);
        });
    }
}
