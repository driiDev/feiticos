package br.com.biblioteca.feiticos.controller;

import br.com.biblioteca.feiticos.dtos.request.EditarFeiticoRequest;
import br.com.biblioteca.feiticos.dtos.request.CadastrarFeiticoRequest;
import br.com.biblioteca.feiticos.dtos.response.FeiticoResponse;
import br.com.biblioteca.feiticos.service.FeiticoService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/feiticos")
@RequiredArgsConstructor
public class FeiticoController {

    private final FeiticoService feiticoService;

    @ApiResponse(responseCode = "201", description = "Feitiço criado com sucesso")
    @PostMapping
    public ResponseEntity<FeiticoResponse> cadastrar(@Valid @RequestBody CadastrarFeiticoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(feiticoService.salvarFeitico(request));
    }

    @GetMapping
    public ResponseEntity<List<FeiticoResponse>> listar(){
        return ResponseEntity.ok(feiticoService.listarFeiticos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FeiticoResponse> buscar(@PathVariable Long id){
        return ResponseEntity.ok(feiticoService.buscarFeitico(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<FeiticoResponse> editar(@PathVariable Long id, @Valid @RequestBody EditarFeiticoRequest request){
        return ResponseEntity.ok(feiticoService.editarFeitico(id, request));
    }

    @ApiResponse(responseCode = "204", description = "feitiço deletado com sucesso")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id){
        feiticoService.deletarFeitico(id);
        return ResponseEntity.noContent().build();
    }
}