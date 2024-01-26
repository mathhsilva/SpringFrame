package com.example.Product.controllers;

import br.framework.classes.DataBase.fields.Description;
import br.framework.classes.model.Unidades;
import br.framework.interfaces.IConnection;
import com.example.Product.business.ProdutoBusiness;
import com.example.Product.business.UnidadeBusiness;
import com.example.Product.helper.ConnectionManager;
import com.example.Product.model.dto.ProdutoDto;
import com.example.Product.model.dto.UnidadeDto;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/unidades")
public class UnidadeController {

    @GetMapping("/")
    public String index() {
        return "index";
    }
    @GetMapping("")
    public List<UnidadeDto> getUnidades() {
        return UnidadeBusiness.getUnidadesRequest();
    }
    @GetMapping("{cnpj}")
    public UnidadeDto getUnidade(@PathVariable String cnpj) {
        return UnidadeBusiness.getUnidadeRequest(cnpj);
    }

    @PostMapping ("/insert")
    public UnidadeDto postUnidade(@RequestBody UnidadeDto unidade) {
        return UnidadeBusiness.criarUnidade(unidade);
    }
}
