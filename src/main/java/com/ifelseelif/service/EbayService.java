package com.ifelseelif.service;

import com.ifelseelif.exceptions.HttpException;
import com.ifelseelif.models.Product;

import java.io.IOException;
import java.util.List;

public interface EbayService {
    List<Product> getAllProducts(int manufacturerId) throws HttpException, IOException;

    void increasePrice(int percent) throws HttpException, IOException;
}
