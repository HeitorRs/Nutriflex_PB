package info.heitor.produtoservice.repository;


import info.heitor.produtoservice.model.HistoricoProduto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoricoProdutoRepository extends JpaRepository<HistoricoProduto, Long> {

    List<HistoricoProduto> findByCodigoProduto(String codigoProduto);
}
