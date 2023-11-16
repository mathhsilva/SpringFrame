package com.example.Product.controllers;

import com.example.Product.business.ProdutoBusiness;
import com.example.Product.model.dto.ProdutoDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    @GetMapping("")
    public List<ProdutoDto> getProdutos() {

        return ProdutoBusiness.getProdutosRequest();
    }

    @GetMapping("/{id}")
    public ProdutoDto getProduto(@PathVariable String id) {
        return ProdutoBusiness.getProdutoRequest(id);
    }

    @PostMapping("")
    public ResponseEntity<String> criarProduto(@RequestBody ProdutoDto novoPoroduto) {
        return  ProdutoBusiness.getProdutoRequest();
    }

}