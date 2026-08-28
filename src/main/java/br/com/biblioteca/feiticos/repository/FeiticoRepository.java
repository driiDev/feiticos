package br.com.biblioteca.feiticos.repository;

import br.com.biblioteca.feiticos.domain.Feitico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeiticoRepository extends JpaRepository<Feitico, Long> {
}
