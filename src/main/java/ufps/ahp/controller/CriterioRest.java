package ufps.ahp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ufps.ahp.model.Criterio;
import ufps.ahp.model.Problema;
import ufps.ahp.security.dto.Mensaje;
import ufps.ahp.services.CriterioService;

import javax.validation.Valid;

@RequestMapping(value="/criterio", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
//@CrossOrigin(origins = "http://angular-ahp.s3-website.us-east-2.amazonaws.com/")
@CrossOrigin

public class CriterioRest {
    @Autowired
    CriterioService criterioService;

    @GetMapping
    public ResponseEntity<?> listar(){
        return ResponseEntity.ok(criterioService.listar());
    }

    @GetMapping("/{idCriterio}")
    public ResponseEntity<?> encontrar(@PathVariable int idCriterio){
        return ResponseEntity.ok(criterioService.buscar(idCriterio));
    }

    @DeleteMapping(path = "/{idCriterio}")
    public ResponseEntity<?> eliminarCriterio(@PathVariable int idCriterio){

        Criterio p = criterioService.buscar(idCriterio);
        if(p == null){
            return new ResponseEntity(new Mensaje("El Criterio no existe"), HttpStatus.BAD_REQUEST);
        }
        criterioService.eliminar(p);

        return ResponseEntity.ok(new Mensaje("Criterio #"+p.getIdCriterio()+" eliminado"));
    }
    @PutMapping(path = "/{idCriterio}")
    public ResponseEntity<?> editarCriterio(@RequestBody @Valid Criterio criterio, BindingResult br){

        if(br.hasErrors()){
            return new ResponseEntity<>(br.getAllErrors(),HttpStatus.BAD_REQUEST);
        }

        criterioService.guardar(criterio);

        return ResponseEntity.ok(new Mensaje("Criterio editado"));
    }
}
