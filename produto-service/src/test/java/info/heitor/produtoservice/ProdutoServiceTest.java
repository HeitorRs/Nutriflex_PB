package info.heitor.produtoservice;

import info.heitor.produtoservice.filters.ProdutoFiltros;
import info.heitor.produtoservice.model.HistoricoProduto;
import info.heitor.produtoservice.model.Produto;
import info.heitor.produtoservice.repository.HistoricoProdutoRepository;
import info.heitor.produtoservice.repository.ProdutoRepository;
import info.heitor.produtoservice.service.ProdutoService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProdutoServiceTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private HistoricoProdutoRepository historicoProdutoRepository;

    @InjectMocks
    private ProdutoService produtoService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testBuscarFiltros_nomeExistente() {
        ProdutoFiltros filtros = new ProdutoFiltros();
        filtros.setNome("ProdutoTeste");

        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        CriteriaQuery<Produto> cq = mock(CriteriaQuery.class);
        Root<Produto> produto = mock(Root.class);

        when(entityManager.getCriteriaBuilder()).thenReturn(cb);
        when(cb.createQuery(Produto.class)).thenReturn(cq);
        when(cq.from(Produto.class)).thenReturn(produto);
        when(entityManager.createQuery(cq)).thenReturn(mock(TypedQuery.class));

        List<Produto> result = produtoService.buscarFiltros(filtros);

        verify(entityManager).getCriteriaBuilder();
        verify(cq).where(any(Predicate[].class));
    }

    @Test
    public void testListarTodosProdutos() {
        List<Produto> mockProdutos = List.of(new Produto(), new Produto());
        when(produtoRepository.findAll()).thenReturn(mockProdutos);

        List<Produto> produtos = produtoService.listarTodosProdutos();
        assertEquals(2, produtos.size());
    }

    @Test
    public void testSalvarProduto() {
        Produto produto = new Produto();
        produto.setNome("Produto Teste");
        produto.setPreco(new BigDecimal("10.0"));
        produto.setEstoque(5);

        when(produtoRepository.save(any(Produto.class))).thenReturn(produto);

        Produto produtoSalvo = produtoService.salvarProduto(produto);

        verify(historicoProdutoRepository, times(1)).save(any(HistoricoProduto.class));

        assertNotNull(produtoSalvo);
    }

    @Test
    public void testEncontrarProdutoPorId_idValido() {
        Produto produto = new Produto();
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

        Optional<Produto> resultado = produtoService.encontrarProdutoPorId(1L);
        assertNotNull(resultado);
    }

    @Test
    public void testEncontrarProdutoPorId_idInvalido() {
        when(produtoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            produtoService.encontrarProdutoPorId(1L);
        });
    }

    @Test
    public void testDeletarProdutoPorId_idValido() {
        Produto produto = new Produto();
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

        produtoService.deletarProdutoPorId(1L);
        verify(produtoRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeletarProdutoPorId_idInvalido() {
        when(produtoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            produtoService.deletarProdutoPorId(1L);
        });
    }

    @Test
    public void testAtualizarProduto() {
        Produto produtoExistente = new Produto();
        Produto produtoAtualizado = new Produto();
        produtoAtualizado.setNome("Novo Nome");
        produtoAtualizado.setPreco(new BigDecimal("20.0"));
        produtoAtualizado.setEstoque(10);

        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produtoExistente));
        when(produtoRepository.save(any(Produto.class))).thenReturn(produtoAtualizado);

        Produto resultado = produtoService.atualizarProduto(1L, produtoAtualizado);
        assertEquals("Novo Nome", resultado.getNome());
    }

    @Test
    public void testAtualizarProduto_idInvalido() {
        Produto produtoAtualizado = new Produto();
        when(produtoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            produtoService.atualizarProduto(1L, produtoAtualizado);
        });
    }
}
