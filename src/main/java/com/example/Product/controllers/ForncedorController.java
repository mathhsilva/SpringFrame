package com.example.Product.controllers;

import com.example.Product.business.FornecedorBusiness;
import com.example.Product.model.FornecedorDaoImpl;
import com.example.Product.model.dto.FornecedorDto;
import com.example.Product.model.entity.Fornecedor;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("")
    public FornecedorDto postFornecedor(@RequestBody FornecedorDto fornecedor){
        return FornecedorBusiness.criarFornecedor(fornecedor);
    }

}
