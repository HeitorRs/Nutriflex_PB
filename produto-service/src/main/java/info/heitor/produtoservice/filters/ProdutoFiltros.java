package info.heitor.produtoservice.filters;

import info.heitor.produtoservice.model.Categoria;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProdutoFiltros {
    private String nome;
    private Categoria categoria;
}
