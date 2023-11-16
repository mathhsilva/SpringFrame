package com.example.Product.controllers;

import com.example.Product.business.FornecedorBusiness;
import com.example.Product.model.dto.FornecedorDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/fornecedores")
public class ForncedorController {
    @GetMapping("")
    public List<FornecedorDto> getFornecedores() {
        return FornecedorBusiness.getFornecedoresRequest();
    }

    @GetMapping("/{id}")
    public FornecedorDto getFornecedor(@PathVariable Integer id) {
        return FornecedorBusiness.getFornecedorRequest(id);
    }

}
