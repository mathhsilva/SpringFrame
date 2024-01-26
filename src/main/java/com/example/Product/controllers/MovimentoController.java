package com.example.Product.controllers;

import com.example.Product.business.MovimentoBusiness;
import com.example.Product.model.dto.MovimentoDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/movimentos")
public class MovimentoController {
@GetMapping("")
    public List<MovimentoDto> getMovimentos (){
        return MovimentoBusiness.getMovimentosRequest();
    }

}

