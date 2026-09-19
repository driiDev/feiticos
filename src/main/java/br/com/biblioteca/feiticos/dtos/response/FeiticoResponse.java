package br.com.biblioteca.feiticos.dtos.response;

import br.com.biblioteca.feiticos.enums.Tipo;

public record FeiticoResponse(
        Long id,
        String nome,
        String descricao,
        Tipo tipo,
        int nivelDificuldade
) {}