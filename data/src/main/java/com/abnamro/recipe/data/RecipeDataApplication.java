package com.abnamro.recipe.data;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.abnamro.recipe.data.repository")
public class RecipeDataApplication {

	public static void main(String[] args) {
		SpringApplication.run(RecipeDataApplication.class, args);
	}

}
