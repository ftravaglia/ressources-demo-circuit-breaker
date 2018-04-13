package fr.ftravaglia.demo.ressourcesdemocircuitbreaker;

import fr.ftravaglia.demo.ressourcesdemocircuitbreaker.model.Structure;
import fr.ftravaglia.demo.ressourcesdemocircuitbreaker.model.StructureList;
import fr.ftravaglia.demo.ressourcesdemocircuitbreaker.persistence.StructureAcces;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@EnableAutoConfiguration
@SpringBootApplication
public class RessourcesDemoCircuitBreakerApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(RessourcesDemoCircuitBreakerApplication.class);
    
    @Autowired
    private StructureAcces structureAcces;
    
    @RequestMapping(value = "/api/v1/structures/", method = RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
    protected ResponseEntity<List<Structure>> listStructures() {
        final StructureList strList = new StructureList();
        
        for (Structure structures : structureAcces.findAll()) {
            LOGGER.info(structures.toString());
            strList.add(structures);
        }
        
        return new ResponseEntity<>(strList, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/api/v1/structures/add/{num}", method = RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
    protected ResponseEntity<List<Structure>> add(@PathVariable("num") long num) {
        final Instant instant = Instant.now();
        final String time = instant.toString();
        for (int i = 0; i < num; i++) {
            structureAcces.save(new Structure("Structure"+time, "Str"+time, "Description de structure " + time));
        }
        
        final List<Structure> strList = new ArrayList<>();
        for(Structure str : structureAcces.findAll()){
            strList.add(str);
        }
        return new ResponseEntity<>(strList, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/api/v1/structures/create/", method = RequestMethod.POST)
    public ResponseEntity<List<Structure>> createStructure(@RequestBody Structure user, UriComponentsBuilder ucBuilder) {
        LOGGER.info("Creating Structure : {}", user);
        final Long identifiant = user.getId();
        if (identifiant != null && identifiant != 0) {
            final Optional<Structure> findById = structureAcces.findById(identifiant);
            
            if (findById != null && findById.get().getId() != 0) {
                LOGGER.error("Unable to create. A Structure with name {} already exist", user.getName());
                return new ResponseEntity(new IllegalArgumentException("Unable to create. A Structure with name " +
                        user.getName() + " already exist."),HttpStatus.CONFLICT);
            }
        }
        structureAcces.save(user);
 
        final List<Structure> strList = new ArrayList<>();
        for(Structure str : structureAcces.findAll()){
            strList.add(str);
        }
        return new ResponseEntity<>(strList, HttpStatus.CREATED);
    }
 
    
    public static void main(String[] args) throws ClassNotFoundException {
        SpringApplication.run(RessourcesDemoCircuitBreakerApplication.class, args);
    }
    
}