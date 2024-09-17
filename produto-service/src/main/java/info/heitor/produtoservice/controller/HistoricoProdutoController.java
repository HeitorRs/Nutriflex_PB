package info.heitor.produtoservice.controller;


import info.heitor.produtoservice.model.HistoricoProduto;
import info.heitor.produtoservice.model.Produto;
import info.heitor.produtoservice.service.HistoricoProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/historico")
@RequiredArgsConstructor
@Slf4j
public class HistoricoProdutoController {

    private final HistoricoProdutoService historicoProdutoService;

    @GetMapping
    public List<HistoricoProduto> listar() {
        return historicoProdutoService.buscarTodos();
    }


    @Operation(summary = "Encontra histórico do produto pelo código")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto encontrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Código inválido"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    @GetMapping("/{codigoProduto}")
    public ResponseEntity<?> listarPorCodigo(@PathVariable String codigoProduto) {
        try {
            List<HistoricoProduto> historicoProduto = historicoProdutoService.buscarPorId(codigoProduto);
            log.info("Histírico encontrado");
            return new ResponseEntity<>(historicoProduto, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
