package ufps.ahp.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ufps.ahp.model.Alternativa;
import ufps.ahp.services.AlternativaService;

@RequestMapping(value="/alternativa",produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
@CrossOrigin(origins = "http://angular-ahp.s3-website.us-east-2.amazonaws.com/")

public class AlternativaRest {

    @Autowired
    AlternativaService alternativaService;

    @GetMapping
    public ResponseEntity<?> listar(){
        return ResponseEntity.ok(alternativaService.listar());
    }


}
