package ufps.ahp.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ufps.ahp.model.Alternativa;
import ufps.ahp.model.Criterio;
import ufps.ahp.model.Problema;
import ufps.ahp.security.dto.Mensaje;
import ufps.ahp.services.AlternativaService;
import ufps.ahp.services.ProblemaService;

import javax.validation.Valid;

@RequestMapping(value="/alternativa",produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
//@CrossOrigin(origins = "http://angular-ahp.s3-website.us-east-2.amazonaws.com/")
@CrossOrigin

public class AlternativaRest {

    @Autowired
    AlternativaService alternativaService;

    @Autowired
    ProblemaService problemaService;

    @GetMapping
    public ResponseEntity<?> listar(){
        return ResponseEntity.ok(alternativaService.listar());
    }

    @GetMapping("/{idAlternativa}")
    public ResponseEntity<?> encontrarAlternativa(@PathVariable int idAlternativa){
        return ResponseEntity.ok(alternativaService.buscar(idAlternativa));
    }

    @PutMapping()
    public ResponseEntity<?> editar(@RequestBody @Valid Alternativa alternativa, BindingResult br){

        if(br.hasErrors()){
            return new ResponseEntity<>(br.getAllErrors(), HttpStatus.BAD_REQUEST);
        }

        alternativaService.guardar(alternativa);
        return ResponseEntity.ok(new Mensaje("alternativa editada"));
    }

    @PostMapping
    public ResponseEntity<?> guardar(@RequestBody @Valid Alternativa alternativa, BindingResult br){

        if(br.hasErrors()){
            return new ResponseEntity<>(br.getAllErrors(),HttpStatus.BAD_REQUEST);
        }

        if(alternativa == null){
            return new ResponseEntity(new Mensaje("Datos incorrectos"),HttpStatus.BAD_REQUEST);
        }


        Problema problema = problemaService.buscar(alternativa.getProblema().getToken());
        alternativa.setProblema(problema);
        alternativaService.guardar(alternativa);

        return ResponseEntity.ok(new Mensaje("alternativa creado"));
    }

}
