package br.com.biblioteca.feiticos.controller;

import br.com.biblioteca.feiticos.dtos.request.FeiticoEdicaoRequestDTO;
import br.com.biblioteca.feiticos.dtos.request.FeiticoRequestDTO;
import br.com.biblioteca.feiticos.dtos.response.FeiticoResponseDTO;
import br.com.biblioteca.feiticos.service.FeiticoService;
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

    @PostMapping
    public ResponseEntity<FeiticoResponseDTO> cadastrar(@Valid @RequestBody FeiticoRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(feiticoService.salvarFeitico(request));
    }

    @GetMapping
    public ResponseEntity<List<FeiticoResponseDTO>> listarTodos(){
        return ResponseEntity.ok(feiticoService.listarFeiticos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FeiticoResponseDTO> buscar(@PathVariable Long id){
        return ResponseEntity.ok(feiticoService.buscarFeitico(id));
    }

    @PatchMapping("/editar/{id}")
    public ResponseEntity<FeiticoResponseDTO> editar(@PathVariable Long id, @RequestBody FeiticoEdicaoRequestDTO request){
        return ResponseEntity.ok(feiticoService.editarFeitico(id, request));
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id){
        feiticoService.deletarFeitico(id);
        return ResponseEntity.noContent().build();
    }
}