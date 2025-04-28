package com.ecommerce.backendnpu.repository;

import com.ecommerce.backendnpu.model.PedidoItem;
import com.ecommerce.backendnpu.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemsPedidoRepository extends JpaRepository<PedidoItem, Long> {

    List<PedidoItem> findByProducto_Vendedor(Usuario vendedor);
}
