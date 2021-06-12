package com.cats.app.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cats.app.entities.Cat;

@Repository
public interface CatRepository extends JpaRepository<Cat, Long>{

	List<Cat> findByRace(String race);
	List<Cat> findByPrice(Double price);
	List<Cat> findByPriceGreaterThan (Double price);
	List<Cat> findByPriceLessThan (Double price);
	List<Cat> findByPriceBetween(Double minPrice, Double maxPrice);
}
