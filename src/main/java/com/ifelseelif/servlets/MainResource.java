package com.ifelseelif.servlets;

import com.ifelseelif.exceptions.HttpException;
import com.ifelseelif.models.Body;
import com.ifelseelif.service.EbayService;
import com.ifelseelif.service.EbayServiceImp;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;

@Path("/ebay")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MainResource {
    EbayService ebayService = new EbayServiceImp();

    @Path("/filter/manufacturer/{manufacturer-id}")
    @GET
    public Response getAllProducts(@PathParam("manufacturer-id") int manufacturerId) {
        try {

            File folder = new File(".");
            File[] listOfFiles = folder.listFiles();

            for (File file : listOfFiles) {
                if (file.isFile()) {
                    System.out.println(file.getName());
                }
            }
            return Response.ok(ebayService.getAllProducts(manufacturerId)).build();
        } catch (HttpException e) {
            return Response.status(e.getStatusCode(), e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(500).entity(e.getMessage()).build();
        }
    }

    @Path("/price/increase/{increase-percent}")
    @POST
    public Response increasePrice(@PathParam("increase-percent") int percent) {
        try {
            ebayService.increasePrice(percent);
            return Response.noContent().build();
        } catch (HttpException e) {
            return Response.status(e.getStatusCode()).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(500, e.getMessage()).build();
        }
    }
}