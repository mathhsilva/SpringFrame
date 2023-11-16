package com.example.Product.controllers;

import com.example.Product.business.ClienteBusiness;
import com.example.Product.business.FornecedorBusiness;
import com.example.Product.model.dto.ClienteDto;
import com.example.Product.model.dto.FornecedorDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {
    @GetMapping("")
    public List<ClienteDto> getClientes() {
        return ClienteBusiness.getClientesRequest();
    }
    @GetMapping("/{id}")
    public ClienteDto getCliente(@PathVariable String id) {
        return ClienteBusiness.getClienteRequest(id);
    }
}
