package com.example.Product.controllers;

import com.example.Product.Errors.InternalServerError;
import com.example.Product.business.ProdutoBusiness;
import com.example.Product.model.ProdutoDao;
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
    public ProdutoDto getProduto(@PathVariable Integer id) {
        return ProdutoBusiness.getProdutoRequest(id);
    }

    @PostMapping("")
    public ProdutoDto postProduto(@RequestBody ProdutoDto produto) {
        return ProdutoBusiness.criarProduto(produto);
    }

}