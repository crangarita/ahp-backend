package ufps.ahp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ufps.ahp.security.dto.Mensaje;

@RestController
//@CrossOrigin(origins = "http://angular-ahp.s3-website.us-east-2.amazonaws.com/")
@RequestMapping(value= "/status",produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin
public class StatusController {
	
	@GetMapping
	public ResponseEntity<?> status() {
		return new ResponseEntity(new Mensaje("Ok"),HttpStatus.OK);
	}

}
