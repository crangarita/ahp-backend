package ufps.ahp.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ufps.ahp.model.Alternativa;
import ufps.ahp.services.AlternativaService;

@RequestMapping(value="/alternativa",produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
//@CrossOrigin(origins = "http://angular-ahp.s3-website.us-east-2.amazonaws.com/")
@CrossOrigin

public class AlternativaRest {

    @Autowired
    AlternativaService alternativaService;

    @GetMapping
    public ResponseEntity<?> listar(){
        return ResponseEntity.ok(alternativaService.listar());
    }

    @GetMapping("/{idAlternativa}")
    public ResponseEntity<?> encontrarAlternativa(@PathVariable int idAlternativa){
        return ResponseEntity.ok(alternativaService.buscar(idAlternativa));
    }

}
