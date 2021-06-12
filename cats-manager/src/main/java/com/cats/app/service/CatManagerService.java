package com.cats.app.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.cats.app.entities.Cat;

public interface CatManagerService {

	public void saveToDatabase(MultipartFile file);
	public List<Cat> findAll();
	public Page<Cat> findAll(Pageable pageable);
	public List<Cat> findByRace(String race);
	public Optional<Cat> findById(Long id);
	public List<Cat> findByPrice(Double price);
	public List<Cat> findByPriceGreaterThan (Double price);
	public List<Cat> findByPriceLessThan (Double price);
	public List<Cat> findByPriceBetween (Double minPrice, Double maxPrice);
	public Cat createCat(Cat cat);
	public void deleteCat(Cat cat);
	public List<Cat> setCatAge(List<Cat> cats);
}
