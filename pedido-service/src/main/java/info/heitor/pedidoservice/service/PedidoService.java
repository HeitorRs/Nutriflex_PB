package info.heitor.pedidoservice.service;

import feign.FeignException;
import info.heitor.pedidoservice.model.*;
import info.heitor.pedidoservice.repository.PedidoRepository;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProdutoService produtoService;
    private final DescontoService descontoService;
    private final Tracer tracer;


    public Pedido criarPedido(Pedido pedido) {
        Span currentSpan = tracer.currentSpan();
        String traceId = currentSpan != null ? currentSpan.context().traceId() : "N/A";
        pedido.setTraceId(traceId);
        pedido.criarSerial();
        validarPedido(pedido);
        pedido.setValorTotal(valorTotalPedido(pedido));
        pedido.setValorTotalComDesconto(calcularValorTotalComDesconto(pedido));
        pedido.setStatus(PedidoStatus.CRIADO);
        return pedidoRepository.save(pedido);
    }

    public List<Pedido> buscarTodosPedidos() {
        return pedidoRepository.findAll();
    }

    public BigDecimal valorTotalPedido(Pedido pedido) {
        return pedido.getItens().stream().map(this::valorTotalItem)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal valorTotalItem(ItemPedido itemPedido) {
        BigDecimal valorTotal = BigDecimal.ZERO;
        try {
            Produto produto = produtoService.findById(itemPedido.getProdutoId());
            if (produto.getEstoque() < itemPedido.getQuantidade()) {
                throw new IllegalArgumentException("Estoque indisponivel! quantidade existente para produto: " + produto.getEstoque());
            }
            valorTotal = produto.getPreco().multiply(BigDecimal.valueOf(itemPedido.getQuantidade()));
        }catch (FeignException e) {
            if (e.status() == 400 || e.status() == 404) {
                throw new IllegalArgumentException("ID do produto é inválido");
            }
        }
        return valorTotal;
    }

    public Optional<Pedido> procurarPorId(String id) {
        if (id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID inválido!");
        }
        return Optional.ofNullable(pedidoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Produto não encontrado para o ID: " + id)));
    }

    public void cancelarPedido(String id) {
        Pedido pedidoExistente = pedidoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Pedido não encontrado"));

        pedidoExistente.setStatus(PedidoStatus.CANCELADO);

        pedidoRepository.save(pedidoExistente);
    }

    public BigDecimal calcularValorTotalComDesconto(Pedido pedido) {
        if (pedido.getCupom() != null && !pedido.getCupom().isEmpty()) {
            try {
                DescontoResponsePayload valorComDesconto = descontoService.getTotalDesconto(pedido);
                BigDecimal result = valorComDesconto.getValorTotalComDesconto();
                pedido.setValorTotalComDesconto(result);
            } catch (FeignException e) {
                if (e.status() == 400) {
                    throw new IllegalArgumentException("Cupom inválido");
                }
            }
        } else {
            pedido.setValorTotalComDesconto(null);
        }
        return pedido.getValorTotalComDesconto();
    }

    private void validarPedido(Pedido pedido) {
        if (pedido.getItens() == null || pedido.getItens().isEmpty()) {
            throw new IllegalArgumentException("O pedido deve conter pelo menos um item.");
        }
        for (ItemPedido item : pedido.getItens()) {
            if (item.getProdutoId() <= 0) {
                throw new IllegalArgumentException("ID do produto inválido: " + item.getProdutoId());
            }
            if (item.getQuantidade() <= 0) {
                throw new IllegalArgumentException("Quantidade inválida para o produto ID: " + item.getProdutoId());
            }
        }
    }
}
