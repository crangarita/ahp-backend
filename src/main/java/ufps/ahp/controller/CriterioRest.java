package ufps.ahp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ufps.ahp.model.Criterio;
import ufps.ahp.model.Problema;
import ufps.ahp.services.CriterioService;

import javax.validation.Valid;

@RequestMapping("/criterio")
@RestController
@CrossOrigin(origins = "http://ahp-env.eba-mumapkxa.us-east-1.elasticbeanstalk.com/")

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
            return new ResponseEntity("El Criterio no existe", HttpStatus.BAD_REQUEST);
        }
        criterioService.eliminar(p);

        return ResponseEntity.ok("Criterio #"+p.getIdCriterio()+" eliminado");
    }
    @PutMapping(path = "/{idCriterio}")
    public ResponseEntity<?> editarCriterio(@RequestBody @Valid Criterio criterio, BindingResult br){

        if(br.hasErrors()){
            return new ResponseEntity<>(br.getAllErrors(),HttpStatus.BAD_REQUEST);
        }

        criterioService.guardar(criterio);

        return ResponseEntity.ok("Criterio editado");
    }
}
