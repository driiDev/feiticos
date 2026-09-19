package br.com.biblioteca.feiticos.service;

import br.com.biblioteca.feiticos.domain.Feitico;
import br.com.biblioteca.feiticos.dtos.request.EditarFeiticoRequest;
import br.com.biblioteca.feiticos.dtos.request.CadastrarFeiticoRequest;
import br.com.biblioteca.feiticos.dtos.response.FeiticoResponse;
import br.com.biblioteca.feiticos.exception.FeiticoNaoEncontradoException;
import br.com.biblioteca.feiticos.mapper.FeiticoMapper;
import br.com.biblioteca.feiticos.repository.FeiticoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeiticoService {

    private final FeiticoRepository feiticoRepository;
    private final ChatModel chatModel;

    public FeiticoResponse salvarFeitico(CadastrarFeiticoRequest request) {

        String descricaoFinal = request.getDescricao();
        Integer nivelDificuldadeFinal = request.getNivelDificuldade();

        if (descricaoFinal == null || descricaoFinal.isBlank()) {

            String prompt = "Você é um grimório técnico e enciclopédico do universo de magia. "
                    + "Gere uma descrição OBJETIVA e FOCADA NO EFEITO PRÁTICO para o feitiço: " + request.getNome()
                    + " que pertence à categoria: " + request.getTipo() + ". "
                    + "A descrição deve explicar puramente o que o feitiço FAZ (Exemplo para 'Aberto': 'Abre portas e janelas trancadas'). "
                    + "ATENÇÃO: Seja direto, use no máximo 200 caracteres e não adicione nenhuma saudação ou texto extra.";

            String respostaIA = chatModel.call(prompt);

            descricaoFinal = respostaIA;
        }

        if (nivelDificuldadeFinal == null) {
                nivelDificuldadeFinal = 3;
        }

        Feitico feitico = new Feitico();
        feitico.setNome(request.getNome());
        feitico.setTipo(request.getTipo());
        feitico.setDescricao(descricaoFinal);
        feitico.setNivelDificuldade(nivelDificuldadeFinal);

        Feitico feiticoSalvo = feiticoRepository.save(feitico);

        return FeiticoMapper.toResponse(feiticoSalvo);
    }

    public List<FeiticoResponse> listarFeiticos(){

        List<Feitico> feiticos = feiticoRepository.findAll();

        return feiticos.stream()
                .map(FeiticoMapper::toResponse).toList();
    }

    public FeiticoResponse buscarFeitico(long id){

        Feitico feitico = feiticoRepository.findById(id)
                .orElseThrow(() -> new FeiticoNaoEncontradoException("Feitiço não encontrado"));

        return FeiticoMapper.toResponse(feitico);

    }

    public FeiticoResponse editarFeitico(long id, EditarFeiticoRequest request){

        Feitico feitico = feiticoRepository.findById(id)
                .orElseThrow(() -> new FeiticoNaoEncontradoException("Feitiço não encontrado"));

        if(request.getNome() != null){
             feitico.setNome(request.getNome());
        }
        if(request.getDescricao() != null){
            feitico.setDescricao(request.getDescricao());
        }
        if (request.getTipo() != null){
            feitico.setTipo(request.getTipo());
        }
        if (request.getNivelDificuldade() != null){
            feitico.setNivelDificuldade(request.getNivelDificuldade());
        }

        feiticoRepository.save(feitico);

        return FeiticoMapper.toResponse(feitico);
    }

    public void deletarFeitico(Long id){

        Feitico feitico = feiticoRepository.findById(id)
                .orElseThrow(() -> new FeiticoNaoEncontradoException("Feitiço não encontrado."));

        feiticoRepository.delete(feitico);
    }
}