package br.com.biblioteca.feiticos.service;

import br.com.biblioteca.feiticos.domain.Feitico;
import br.com.biblioteca.feiticos.dtos.request.FeiticoRequestDTO;
import br.com.biblioteca.feiticos.dtos.response.FeiticoResponseDTO;
import br.com.biblioteca.feiticos.repository.FeiticoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeiticoService {

    private final FeiticoRepository feiticoRepository;
    private final ChatModel chatModel;

    public FeiticoResponseDTO salvarFeitico(FeiticoRequestDTO request) {

        String descricaoFinal = request.getDescricao();
        Integer nivelDificuldadeFinal = request.getNivelDificuldade();

        if (descricaoFinal == null || descricaoFinal.isBlank() || nivelDificuldadeFinal == null) {

            String prompt = "Você é um grimório técnico e enciclopédico do universo de magia. "
                    + "Gere uma descrição OBJETIVA e FOCADA NO EFEITO PRÁTICO para o feitiço: " + request.getNome()
                    + " que pertence à categoria: " + request.getTipo() + ". "
                    + "A descrição deve explicar puramente o que o feitiço FAZ (Exemplo para 'Aberto': 'Abre portas e janelas trancadas'). "
                    + "ATENÇÃO: Seja direto, use no máximo 200 caracteres e não adicione nenhuma saudação ou texto extra.";

            String respostaIA = chatModel.call(prompt);

            descricaoFinal = respostaIA;

            if (nivelDificuldadeFinal == null) {
                nivelDificuldadeFinal = 3;
            }
        }

        Feitico feitico = new Feitico();
        feitico.setNome(request.getNome());
        feitico.setTipo(request.getTipo());
        feitico.setDescricao(descricaoFinal);
        feitico.setNivelDificuldade(nivelDificuldadeFinal);

        Feitico feiticoSalvo = feiticoRepository.save(feitico);

        return new FeiticoResponseDTO(
                feiticoSalvo.getId(),
                feiticoSalvo.getNome(),
                feiticoSalvo.getDescricao(),
                feiticoSalvo.getTipo(),
                feiticoSalvo.getNivelDificuldade()
        );
    }
}