package com.ifelseelif.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ifelseelif.exceptions.HttpException;
import com.ifelseelif.models.Product;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;

public class EbayServiceImp implements EbayService {
    @Override
    public List<Product> getAllProducts(int manufacturerId) throws HttpException, IOException {
        if (manufacturerId < 0) {
            throw new HttpException("manufacturerId can't be less than zero", 400);
        }

        ArrayList<Product> products = new ArrayList<>();
        long skip =0;
        while (true){
            WebTarget target = getTarget();
            String json = target.path("api").path("products").queryParam("manufacturer.id", "=;"+manufacturerId)
                    .queryParam("pageIndex",skip).request(MediaType.APPLICATION_JSON_TYPE).get(String.class);
            ObjectMapper objectMapper =new ObjectMapper();
            List<Product> productList = objectMapper.readValue(json, new TypeReference<List<Product>>() {});
            products.addAll( productList);

            skip += productList.size();
            if( productList.size() == 0) {
                break;
            }
        }

        return products;
    }

    @Override
    public void increasePrice(int percent) throws HttpException, IOException {
        if (percent < 0) {
            throw new HttpException("manufacturerId can't be less than zero", 400);
        }

        long skip =0;
        while (true){
            WebTarget target = getTarget();
            String json = target.path("api").path("products")
                    .queryParam("pageIndex",skip).request(MediaType.APPLICATION_JSON_TYPE).get(String.class);
            ObjectMapper objectMapper =new ObjectMapper();
            List<Product> productList = objectMapper.readValue(json, new TypeReference<List<Product>>() {});
            for (Product p:
                    productList) {
                WebTarget target2 = getTarget();
                p.setPrice(p.getPrice() + (p.getPrice() / 100f * percent) );
                Response response = target2.path("api").path("products").path("/"+p.getId()).request().put(Entity.entity(p, MediaType.APPLICATION_JSON));
            }
            skip += productList.size();
            if( productList.size() == 0) {
                break;
            }
        }
    }

    private WebTarget getTarget() throws HttpException {
        String BACK_2_URI = System.getenv("HTTPS_URI");
        try {
            FileInputStream is = new FileInputStream("truststore.jks");
            KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
            keystore.load(is, "secret".toCharArray());
            Client client = ClientBuilder.newBuilder().trustStore(keystore).build();
            return client.target(BACK_2_URI);
        } catch (IOException e) {
            throw new HttpException("Can't find truststore file", 500);
        } catch (Exception e) {
            throw new HttpException("Can't setup SSL", 500);
        }
    }
}

