/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ftravaglia.demo.ressourcesdemocircuitbreaker.persistence;

import fr.ftravaglia.demo.ressourcesdemocircuitbreaker.model.Structure;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author ftravaglia
 */
public interface StructureAcces extends CrudRepository<Structure, Long> {

}
