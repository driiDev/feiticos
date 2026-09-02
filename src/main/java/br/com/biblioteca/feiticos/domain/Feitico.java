package br.com.biblioteca.feiticos.domain;

import br.com.biblioteca.feiticos.enums.Tipo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Feitico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Enumerated(EnumType.STRING)
    private Tipo tipo;

    private Integer nivelDificuldade;
}
