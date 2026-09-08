package com.ai.ecommerce;

import com.ai.ecommerce.model.Category;
import com.ai.ecommerce.model.Product;
import com.ai.ecommerce.repository.CategoryRepository;
import com.ai.ecommerce.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class EcommerceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcommerceApplication.class, args);
    }

    @Bean
    CommandLineRunner initData(CategoryRepository categoryRepository, ProductRepository productRepository) {
        return args -> {
            if (categoryRepository.count() == 0) {
                Category cappuccino = new Category(); cappuccino.setName("Cappuccino");
                Category macchiato = new Category(); macchiato.setName("Macchiato");
                Category latte = new Category(); latte.setName("Latte");
                Category americano = new Category(); americano.setName("Americano");

                categoryRepository.saveAll(List.of(cappuccino, macchiato, latte, americano));

                if (productRepository.count() == 0) {
                    Product p1 = new Product();
                    p1.setTitle("Caffe Mocha");
                    p1.setDescription("Deep Product with Oat Milk");
                    p1.setPrice(4.53);
                    p1.setRating(4.8);
                    p1.setImage("https://images.unsplash.com/photo-1517256064527-09c73fc73e38?w=500&q=80");
                    p1.setCategory(cappuccino);

                    Product p2 = new Product();
                    p2.setTitle("Flat White");
                    p2.setDescription("Espresso with steamed milk");
                    p2.setPrice(3.53);
                    p2.setRating(4.5);
                    p2.setImage("https://images.unsplash.com/photo-1577968897966-3d4325b36b61?w=500&q=80");
                    p2.setCategory(cappuccino);

                    Product p3 = new Product();
                    p3.setTitle("Caramel Macchiato");
                    p3.setDescription("Fresh espresso with vanilla syrup and caramel drizzle");
                    p3.setPrice(5.20);
                    p3.setRating(4.9);
                    p3.setImage("https://images.unsplash.com/photo-1485808191679-5f86510681a2?w=500&q=80");
                    p3.setCategory(macchiato);

                    Product p4 = new Product();
                    p4.setTitle("Iced Hazelnut Latte");
                    p4.setDescription("Rich espresso blended with creamy hazelnut and ice");
                    p4.setPrice(4.80);
                    p4.setRating(4.7);
                    p4.setImage("https://images.unsplash.com/photo-1461023058943-07fcbe16d735?w=500&q=80");
                    p4.setCategory(latte);

                    Product p5 = new Product();
                    p5.setTitle("Classic Americano");
                    p5.setDescription("Bold espresso diluted with hot water");
                    p5.setPrice(3.00);
                    p5.setRating(4.3);
                    p5.setImage("https://images.unsplash.com/photo-1514432324607-a09d9b4aefdd?w=500&q=80");
                    p5.setCategory(americano);

                    productRepository.saveAll(List.of(p1, p2, p3, p4, p5));
                }
            }
        };
    }
}
