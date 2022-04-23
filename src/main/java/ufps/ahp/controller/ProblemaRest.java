package ufps.ahp.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ufps.ahp.model.Alternativa;
import ufps.ahp.model.Criterio;
import ufps.ahp.model.Problema;
import ufps.ahp.model.PuntuacionCriterio;
import ufps.ahp.security.dto.Mensaje;
import ufps.ahp.security.model.Usuario;
import ufps.ahp.security.servicio.UsuarioService;
import ufps.ahp.services.AlternativaService;
import ufps.ahp.services.CriterioService;
import ufps.ahp.services.ProblemaService;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "http://ahp-env.eba-mumapkxa.us-east-1.elasticbeanstalk.com/")
@RequestMapping(value="/problema" ,produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j

public class ProblemaRest {

    @Autowired
    ProblemaService problemaService;

    @Autowired
    CriterioService criterioService;

    @Autowired
    AlternativaService alternativaService;

    @Autowired
    UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<?> listar(){
        return ResponseEntity.ok(problemaService.listar());
    }

    @CrossOrigin
    @GetMapping(path = "/{idProblema}")
    public ResponseEntity<?> encontrarProblema(@PathVariable String idProblema){
        log.info(idProblema);
        return ResponseEntity.ok(problemaService.buscar(idProblema));
    }

    @PostMapping
    public ResponseEntity<?> guardar(@RequestBody @Valid Problema problema, BindingResult br){

        if(br.hasErrors()){
            return new ResponseEntity<>(br.getAllErrors(),HttpStatus.BAD_REQUEST);
        }

        if(problema == null){
            return new ResponseEntity(new Mensaje("Datos incorrectos"),HttpStatus.BAD_REQUEST);
        }
        problema.setIdProblema(UUID.randomUUID().toString());
        problema.setFechaCreacion(new Date());
        problemaService.guardar(problema);
        return ResponseEntity.ok(new Mensaje("Problema creado"));
    }
    @DeleteMapping(path = "/{idProblema}")
    public ResponseEntity<?> eliminarProblema(@PathVariable String idProblema){

        Problema p = problemaService.buscar(idProblema);
        if(p == null){
            return new ResponseEntity(new Mensaje("El problema no existe"),HttpStatus.BAD_REQUEST);
        }
        problemaService.eliminar(p);

        return ResponseEntity.ok(new Mensaje("Problema #"+p.getIdProblema()+" eliminado"));
    }

    @PutMapping
    public ResponseEntity<?> editar(@RequestBody @Valid Problema problema, BindingResult br){

        if(br.hasErrors()){
            return new ResponseEntity<>(br.getAllErrors(),HttpStatus.BAD_REQUEST);
        }

        if(problema == null){
            return new ResponseEntity(new Mensaje("Datos incorrectos"),HttpStatus.BAD_REQUEST);
        }

        problema.setUsuario(usuarioService.getById(problema.getUsuario().getIdUsuario()).orElse(null));
        problemaService.guardar(problema);
        return ResponseEntity.ok("Problema creado");
    }

    @GetMapping(path ="/criterios/{idProblema}")
    public ResponseEntity<?> criteriorPorProblema(@PathVariable String idProblema){
        log.info(idProblema);
        return ResponseEntity.ok(problemaService.buscar(idProblema).criterioCollection());
    }

    @GetMapping(path ="/alternativas/{idProblema}")
    public ResponseEntity<?> alternativaPorProblema(@PathVariable String idProblema){
        return ResponseEntity.ok(problemaService.buscar(idProblema).alternativaCollection());
    }

    @PostMapping(path="/criterios/{idProblema}")
    public ResponseEntity<?> agregarCriteriosDeProblema(@PathVariable String idProblema, @RequestBody List<Criterio> criterios){

        Problema p = problemaService.buscar(idProblema);
        for(Criterio c: criterios){
            c.setProblema(p);
            criterioService.guardar(c);
        }
        return ResponseEntity.ok(criterios);
    }

    @PostMapping(path="/alternativas/{idProblema}")
    public ResponseEntity<?> agregarAlternativasDeProblema(@PathVariable String idProblema, @RequestBody List<Alternativa> alternativas){
        Problema p = problemaService.buscar(idProblema);
        for(Alternativa alt: alternativas){
            alt.setProblema(p);
            alternativaService.guardar(alt);
        }
        return ResponseEntity.ok(alternativas);
    }
}
