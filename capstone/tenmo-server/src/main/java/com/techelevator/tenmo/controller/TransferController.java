package com.techelevator.tenmo.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class TransferController {


    @RequestMapping (path = "/transfers/{id}", method = RequestMethod.GET)
    public boolean getById(@Valid @PathVariable int id){
        return true;
    }
}
