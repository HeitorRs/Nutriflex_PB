package info.heitor.pedidoservice;

import info.heitor.pedidoservice.model.*;
import info.heitor.pedidoservice.repository.PedidoRepository;
import info.heitor.pedidoservice.service.DescontoService;
import info.heitor.pedidoservice.service.PedidoService;
import info.heitor.pedidoservice.service.ProdutoService;
import io.micrometer.tracing.Tracer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static com.mongodb.assertions.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class PedidoServiceTests {
    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private ProdutoService produtoService;

    @Mock
    private DescontoService descontoService;

    @Mock
    private Tracer tracer;

    @InjectMocks
    private PedidoService pedidoService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCriarPedido() {
        Pedido pedido = new Pedido();
        ItemPedido itemPedido = new ItemPedido();
        itemPedido.setProdutoId(1L);
        itemPedido.setQuantidade(2);
        pedido.setItens(List.of(itemPedido));

        Produto produto = new Produto();
        produto.setPreco(new BigDecimal("100"));
        produto.setEstoque(50);

        when(produtoService.findById(anyLong())).thenReturn(produto);
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer((Answer<Pedido>) invocation -> {
            Pedido savedPedido = invocation.getArgument(0);
            savedPedido.setId("123");
            return savedPedido;
        });

        Pedido pedidoCriado = pedidoService.criarPedido(pedido);

        assertNotNull(pedidoCriado);
        assertEquals(PedidoStatus.CRIADO, pedidoCriado.getStatus());
        assertEquals(new BigDecimal("200"), pedidoCriado.getValorTotal());
        verify(pedidoRepository, times(1)).save(any(Pedido.class));
    }

    @Test
    public void testBuscarTodosPedidos() {
        Pedido pedido1 = new Pedido();
        Pedido pedido2 = new Pedido();

        when(pedidoRepository.findAll()).thenReturn(List.of(pedido1, pedido2));

        List<Pedido> pedidos = pedidoService.buscarTodosPedidos();

        assertNotNull(pedidos);
        assertEquals(2, pedidos.size());
        verify(pedidoRepository, times(1)).findAll();
    }

    @Test
    public void testCancelarPedido() {
        Pedido pedido = new Pedido();
        pedido.setId("123");
        pedido.setStatus(PedidoStatus.CRIADO);

        when(pedidoRepository.findById("123")).thenReturn(Optional.of(pedido));

        pedidoService.cancelarPedido("123");

        assertEquals(PedidoStatus.CANCELADO, pedido.getStatus());
        verify(pedidoRepository, times(1)).save(pedido);
    }

    @Test
    public void testCalcularValorTotalComDesconto() {
        Pedido pedido = new Pedido();
        pedido.setCupom("PROMO10");
        pedido.setValorTotal(new BigDecimal("100"));

        DescontoResponsePayload descontoResponse = new DescontoResponsePayload();
        descontoResponse.setValorTotalComDesconto(new BigDecimal("90"));

        when(descontoService.getTotalDesconto(pedido)).thenReturn(descontoResponse);

        BigDecimal valorComDesconto = pedidoService.calcularValorTotalComDesconto(pedido);

        assertNotNull(valorComDesconto);
        assertEquals(new BigDecimal("90"), valorComDesconto);
        verify(descontoService, times(1)).getTotalDesconto(pedido);
    }
}
