package info.heitor.pedidoservice.controller;

import info.heitor.pedidoservice.model.Pedido;
import info.heitor.pedidoservice.service.PagamentoService;
import info.heitor.pedidoservice.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/")
@Slf4j
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;
    private final PagamentoService pagamentoService;

    @PostMapping
    @Operation(summary = "Cria um novo pedido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido criado"),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos")
    })
    public ResponseEntity<?> criarPedido(@RequestBody Pedido pedido) {
        try {
            log.info("Criando pedido");
            Pedido pedidoCriado = pedidoService.criarPedido(pedido);
            log.info("Pedido criado com sucesso: {}", pedidoCriado);
            pagamentoService.processarPagamento(pedidoCriado);
            log.info("Pedido enviado para pagamento");
            return new ResponseEntity<>(pedidoCriado, HttpStatus.OK);
        }catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    @Operation(summary = "Busca todos os pedidos", description = "Recupera todos os pedidos armazenados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedidos encontrados com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<List<Pedido>> buscarTodosPedidos() {
        try {
            List<Pedido> pedidos = pedidoService.buscarTodosPedidos();
            return new ResponseEntity<>(pedidos, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um pedido por ID", description = "Recupera um pedido existente pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    public ResponseEntity<?> buscarPedidoPorId(@PathVariable("id") String id) {
        try{
            log.info("Buscando pedido de id: {}", id);
            Optional<Pedido> pedido = pedidoService.procurarPorId(id);
            log.info("Pedido encontrado com sucesso: {}", pedido);
            return new ResponseEntity<>(pedido, HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
   }

    @DeleteMapping("/{id}")
    @Operation(summary = "Cancela um pedido existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido cancelado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    public ResponseEntity<?> cancelarPedido(@PathVariable("id") String id) {
        try {
            log.info("Cancelando pedido de id: {}", id);
            pedidoService.cancelarPedido(id);
            log.info("Pedido cancelado com sucesso");
            return new ResponseEntity<>("Pedido cancelado com sucesso", HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
