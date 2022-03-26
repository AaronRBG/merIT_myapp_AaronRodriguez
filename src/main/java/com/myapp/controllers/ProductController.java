package com.myapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
@RequestMapping(path = "/product/{productId}")
public class ProductController {

	@Autowired
	private RestTemplate restTemplate;

	@GetMapping("/similar")
	public String getSimilarProducts(@PathVariable int productId) throws JsonProcessingException {
		
		// Retrieve the similar products ids to the given one using the Mocks API
		String aux = restTemplate.getForObject("http://localhost:3001/product/" + productId + "/similarids",
				String.class);
		
		// Parse the string of products ids to an array.		
		aux = aux.replace("[","").replace("]","");		
		String[] products = aux.split(",");
		
		// Open response array
		aux = "[";
		
		// For each productId in the parsed array, add its details to the response array.
		for (int i=0; i<products.length; i++) {
			
			// Retrieve the details of a productId from the Mocks API
			aux += restTemplate.getForObject("http://localhost:3001/product/" + products[i],
					String.class);
			
			// Add array separator
			if(i!=products.length-1) {
				aux += ",";
			}
		}
		
		// Close response array
		aux += "]";
				
		return aux;
	}
	
    @ExceptionHandler(HttpClientErrorException.class)
    public ModelAndView handleProductNotFoundError(Exception ex) {
     
        ModelAndView mav = new ModelAndView("Product Not Found");
        mav.addObject("message", ex.getLocalizedMessage());
         
        return mav;
    } 

}
