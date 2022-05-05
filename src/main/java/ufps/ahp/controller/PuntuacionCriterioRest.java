package ufps.ahp.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ufps.ahp.model.Problema;
import ufps.ahp.services.ProblemaService;
import ufps.ahp.services.PuntuacionServicio;

@RestController
@CrossOrigin
@RequestMapping(value="/puntuacioncriterio" ,produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class PuntuacionCriterioRest {

    @Autowired
    PuntuacionServicio puntuacionServicio;

    @Autowired
    ProblemaService problemaService;


    @GetMapping("/{tokenProblema}")
    public ResponseEntity<?>calcularVectorPropio(@PathVariable String tokenProblema){

        Problema problema = problemaService.buscar(tokenProblema);



        return ResponseEntity.ok("");
    }


}
