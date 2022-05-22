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
import ufps.ahp.services.PuntuacionCriterioServicio;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
//@CrossOrigin(origins = "http://angular-ahp.s3-website.us-east-2.amazonaws.com/")
@CrossOrigin
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

    @Autowired
    PuntuacionCriterioServicio puntuacionCriterioServicio;

    @GetMapping
    public ResponseEntity<?> listar(){
        return ResponseEntity.ok(problemaService.listar());
    }

    @CrossOrigin
    @GetMapping(path = "/{token}")
    public ResponseEntity<?> encontrarProblema(@PathVariable String token){
        return ResponseEntity.ok(problemaService.buscar(token));
    }

    @PostMapping
    public ResponseEntity<?> guardar(@RequestBody @Valid Problema problema, BindingResult br){

        if(br.hasErrors()){
            return new ResponseEntity<>(br.getAllErrors(),HttpStatus.BAD_REQUEST);
        }

        if(problema == null){
            return new ResponseEntity(new Mensaje("Datos incorrectos"),HttpStatus.BAD_REQUEST);
        }
        problema.setToken(UUID.randomUUID().toString());
        problema.setEstado(true);
        problema.setFechaCreacion(new Date());
        problemaService.guardar(problema);
        return ResponseEntity.ok(problema);
    }

    @PostMapping(path="/criterios/{token}")
    public ResponseEntity<?> agregarCriteriosDeProblema(@PathVariable String token, @RequestBody List<Criterio> criterios){

        Problema p = problemaService.buscar(token);
        for(Criterio c: criterios){
            c.setProblema(p);
            criterioService.guardar(c);
        }
        puntuacionCriterioServicio.agregarCriteriosPuntuacion(p.getIdProblema());
        return ResponseEntity.ok(criterios);
    }

    @PostMapping(path="/alternativas/{token}")
    public ResponseEntity<?> agregarAlternativasDeProblema(@PathVariable String token, @RequestBody List<Alternativa> alternativas){
        Problema p = problemaService.buscar(token);
        for(Alternativa alt: alternativas){
            alt.setProblema(p);
            alternativaService.guardar(alt);
        }
        return ResponseEntity.ok(alternativas);
    }

    @DeleteMapping(path = "/{token}")
    public ResponseEntity<?> eliminarProblema(@PathVariable String token){

        Problema p = problemaService.buscar(token);
        if(p == null){
            return new ResponseEntity(new Mensaje("El problema no existe"),HttpStatus.BAD_REQUEST);
        }
        p.setEstado(false);
        problemaService.guardar(p);

        return ResponseEntity.ok(new Mensaje("Problema #"+p.getIdProblema()+" deshabilitado"));
    }

    @GetMapping(path = "/{token}/activar")
    public ResponseEntity<?> activarProblema(@PathVariable String token){

        Problema p = problemaService.buscar(token);
        if(p == null){
            return new ResponseEntity(new Mensaje("El problema no existe"),HttpStatus.BAD_REQUEST);
        }
        p.setEstado(true);
        problemaService.guardar(p);

        return ResponseEntity.ok(new Mensaje("Problema #"+p.getIdProblema()+" deshabilitado"));
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

    @GetMapping(path ="/criterios/{token}")
    public ResponseEntity<?> criteriorPorProblema(@PathVariable String token){
        return ResponseEntity.ok(problemaService.buscar(token).criterioCollection());
    }

    @GetMapping(path ="/alternativas/{token}")
    public ResponseEntity<?> alternativaPorProblema(@PathVariable String token){
        return ResponseEntity.ok(problemaService.buscar(token).alternativaCollection());
    }


    @GetMapping(path="/criteriosComparados/{idProblema}") // Metodo para obtener los pares a comparar en la puntuacion de criterios
    public ResponseEntity<?> obtenerParesCriterios(@PathVariable String idProblema){
        Problema p = problemaService.buscar(idProblema);

        return ResponseEntity.ok(puntuacionCriterioServicio.obtenerParesCriterios(idProblema));
    }
}
