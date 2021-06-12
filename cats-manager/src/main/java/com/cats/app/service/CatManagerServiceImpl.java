package com.cats.app.service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cats.app.dao.CatRepository;
import com.cats.app.entities.Cat;
import com.cats.app.helper.CSVHelper;

@Service
@Transactional
public class CatManagerServiceImpl implements CatManagerService{

	@Autowired
	CatRepository catRepository;
	
	@Override
	public void saveToDatabase(MultipartFile file) {
		// TODO Auto-generated method stub
		try {
			List<Cat> cats = CSVHelper.csvToListOfCats(file.getInputStream());
			catRepository.saveAll(cats);
		} catch (IOException e) {
	      throw new RuntimeException("fail to upload csv data in the database: " + e.getMessage());
	    }
	}

	@Override
	public Page<Cat> findAll(Pageable pageable) {
		// TODO Auto-generated method stub
		return catRepository.findAll(pageable);
	}

	@Override
	public List<Cat> findByRace(String race) {
		// TODO Auto-generated method stub
		return catRepository.findByRace(race);
	}

	@Override
	public List<Cat> findByPriceGreaterThan(Double price) {
		// TODO Auto-generated method stub
		return catRepository.findByPriceGreaterThan(price);
	}

	@Override
	public List<Cat> findByPriceLessThan(Double price) {
		// TODO Auto-generated method stub
		return catRepository.findByPriceLessThan(price);
	}

	@Override
	public List<Cat> findByPrice(Double price) {
		// TODO Auto-generated method stub
		return catRepository.findByPrice(price);
	}

	@Override
	public List<Cat> findByPriceBetween(Double minPrice, Double maxPrice) {
		// TODO Auto-generated method stub
		return catRepository.findByPriceBetween(minPrice, maxPrice);
	}

	@Override
	public Cat createCat(Cat cat) {
		// TODO Auto-generated method stub
		try {
			return catRepository.save(cat);
		}catch(Exception e)
		{
			System.out.println(e.getMessage());
			return null;
		}
	}

	@Override
	public void deleteCat(Cat cat) {
		// TODO Auto-generated method stub
		try {
			catRepository.deleteById(cat.getId());
		}catch(Exception e)
		{
			System.out.println(e.getMessage());
		
		}
	}

	@Override
	public Optional<Cat> findById(Long id) {
		// TODO Auto-generated method stub
		return catRepository.findById(id);
	}

	@Override
	public List<Cat> setCatAge(List<Cat> cats) {
		// TODO Auto-generated method stub
		int age = 0;
		
		for (Cat cat : cats) {
			if ((cat.getBirthDate() != null)) {
				java.sql.Date catBirthDate = java.sql.Date.valueOf(cat.getBirthDate().toString());
	            LocalDate birthDate = catBirthDate.toLocalDate();
				age = Period.between(birthDate, LocalDate.now()).getYears();
				cat.setAge(age);
	        } 
		}
		
		return cats;
		
	}

	@Override
	public List<Cat> findAll() {
		// TODO Auto-generated method stub
		return catRepository.findAll();
	}
	
	
}
