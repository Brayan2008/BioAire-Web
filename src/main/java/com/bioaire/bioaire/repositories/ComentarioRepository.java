package com.bioaire.bioaire.repositories;

import com.bioaire.bioaire.models.Comentario;
import com.bioaire.bioaire.models.Producto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface ComentarioRepository extends JpaRepository<Comentario, Long> {

    List<Comentario> findByProducto(Producto producto);

}
