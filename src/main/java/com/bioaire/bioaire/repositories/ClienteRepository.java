package com.bioaire.bioaire.repositories;

import com.bioaire.bioaire.models.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
