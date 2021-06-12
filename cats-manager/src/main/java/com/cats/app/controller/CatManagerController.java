package com.cats.app.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.cats.app.entities.Cat;
import com.cats.app.helper.CSVHelper;
import com.cats.app.helper.ResponseMessage;
import com.cats.app.service.CatManagerService;


/*
 * @CrossOrigin: Configure the origins which are allowed to call the API.
 * 
 * Author : T.A
 */


@CrossOrigin("http://localhost:9091")
@Controller
@RequestMapping("/cats-api/v1")
public class CatManagerController {

	@Autowired
	CatManagerService catManagerService;
	
	List<Cat> catsWithAge;
	
	/*
	 * This method allow the upload of a CSV file into database
	 * 
	 * Author : T.A
	 */
	@PostMapping("/cat/upload")
	ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile multipartFile){
		
		String message = "";
		
		/*
		 * The file is processed only of it's a CSV file
		 */
		if (CSVHelper.hasCSVFormat(multipartFile)) {
			try {

				/*
				 * call the service to save data in the database
				 */
				catManagerService.saveToDatabase(multipartFile);
				
				// and a sweet message if everything is OK
		        message = "The file has been uploaded successfully :-) " + multipartFile.getOriginalFilename();
		        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
			}catch (Exception e) {
				message = "The file upload has failed ! :( " + multipartFile.getOriginalFilename();
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
			}
		}
		
		message = "Please upload a valid csv file ! ";
	    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
	
	}
	
	/*
	 * 
	 * This function return a list of Cats with pagination
	 * default size of the returned items is size = 5
	 * the default size can be changed by specifying the "size" param in the request header
	 * example : /cats-api/v1/cat/findAllWithPagination?size=<SIZE>
	 * 
	 */
	@GetMapping("/cat/findAllWithPagination")
	public ResponseEntity<Map<String, Object>> catsPageable(@PageableDefault(size = 5) Pageable pageable) { 
		try {
			
			List<Cat> cats = new ArrayList<Cat>();
			Page<Cat> pageCats;
			
			/* Init LIST OF CATS */
			pageCats = catManagerService.findAll(pageable);
			cats = pageCats.getContent();
			
			System.out.println(pageable.getPageSize());
			Map<String, Object> response = new HashMap<>();
			response.put("cats", cats);
		    response.put("currentPage", pageCats.getNumber());
		    response.put("totalItems", pageCats.getTotalElements());
		    response.put("totalPages", pageCats.getTotalPages());

			
			return new ResponseEntity<>(response, HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	

	/*
	 * 
	 * This function return a list of Cats by race
	 * the query param 'RACE' allow a filter of cats by race
	 * example : /cats-api/v1/cat/findByRace?race=<RACE>
	 * 
	 */
	@GetMapping("/cat/findByRace")
	public ResponseEntity<Map<String, Object>> catsByRace(@RequestParam(required = true) String race) { 
		Map<String, Object> response = new HashMap<>();
		
		/*
		 * Check if race is not blank
		 */
		if (race.trim().equals("")) {
			response.put("message", "Please enter a valid value for RACE parameter !");
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}else {
			try {
				List<Cat> cats = new ArrayList<Cat>();
				cats = catManagerService.findByRace(race);
				
				response.put("cats", cats);
			    			
				return new ResponseEntity<>(response, HttpStatus.OK);
			}catch(Exception e) {
				return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
	}
	
	/*
	 * 
	 * This function return a list of Cats by price
	 * the query param 'PRICE' allow a filter of cats by price
	 * example : /cats-api/v1/cat/findByPrice?price=<PRICE>
	 * 
	 */
	@GetMapping("/cat/findByPrice")
	public ResponseEntity<Map<String, Object>> catsByPrice(@RequestParam(required = true) Double price) { 
		Map<String, Object> response = new HashMap<>();
		
		/*
		 * Check if price valid
		 */
		if (price == null || price < 0) {
			response.put("message", "Please enter a valid value for PRICE parameter !");
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}else {
			try {
				List<Cat> cats = new ArrayList<Cat>();
				cats = catManagerService.findByPrice(price);
				
				response.put("cats", cats);
			    			
				return new ResponseEntity<>(response, HttpStatus.OK);
			}catch(Exception e) {
				return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
	}
	
	/*
	 * 
	 * This function return a list of Cats that have a price greater than the specified parameter "PRICE"
	 * 
	 * example : /cats-api/v1/cat/findByPrice/greaterThan?price=<PRICE>
	 * 
	 */
	@GetMapping("/cat/findByPrice/greaterThan")
	public ResponseEntity<Map<String, Object>> catsByPriceGreater(@RequestParam(required = true) Double price) { 
		Map<String, Object> response = new HashMap<>();
		
		/*
		 * Check if the price is valid
		 */
		if (price == null || price < 0) {
			response.put("message", "Please enter a valid value for PRICE parameter !");
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}else {
			try {
				List<Cat> cats = new ArrayList<Cat>();
				cats = catManagerService.findByPriceGreaterThan(price);
				
				response.put("cats", cats);
			    			
				return new ResponseEntity<>(response, HttpStatus.OK);
			}catch(Exception e) {
				return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
	}
	
	/*
	 * 
	 * This function return a list of Cats that have a price greater than the specified parameter "PRICE"
	 * 
	 * example : /cats-api/v1/cat/findByPrice/greaterThan?price=<PRICE>
	 * 
	 */
	@GetMapping("/cat/findByPrice/lessThan")
	public ResponseEntity<Map<String, Object>> catsByPriceLess(@RequestParam(required = true) Double price) { 
		Map<String, Object> response = new HashMap<>();
		
		/*
		 * Check if the price is valid
		 */
		if (price == null || price < 0) {
			response.put("message", "Please enter a valid value for PRICE parameter !");
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}else {
			try {
				List<Cat> cats = new ArrayList<Cat>();
				cats = catManagerService.findByPriceLessThan(price);
				
				response.put("cats", cats);
			    			
				return new ResponseEntity<>(response, HttpStatus.OK);
			}catch(Exception e) {
				return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
	}
	
	/*
	 * 
	 * This function return a list of Cats that have a price between the specified parameters "minPRICE" & "maxPRICE"
	 * 
	 * example : /cats-api/v1/cat/findByPrice/between?minPrice=<minPRICE>&maxPrice=<maxPRICE>
	 * 
	 */
	@GetMapping("/cat/findByPrice/between")
	public ResponseEntity<Map<String, Object>> catsByPriceLess(@RequestParam(required = true) Double minPrice,
			@RequestParam(required = true) Double maxPrice) { 
		Map<String, Object> response = new HashMap<>();
		
		/*
		 * Check if the price is valid
		 */
		if (minPrice == null || minPrice < 0 ) {
			response.put("message", "Please enter a valid value for minPRICE parameter !");
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}else if (maxPrice == null || maxPrice < 0){
			response.put("message", "Please enter a valid value for maxPRICE parameter !");
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}else
		{
			try {
				List<Cat> cats = new ArrayList<Cat>();
				cats = catManagerService.findByPriceBetween(minPrice, maxPrice);
				
				response.put("cats", cats);
			    			
				return new ResponseEntity<>(response, HttpStatus.OK);
			}catch(Exception e) {
				return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
	}
	
	/*
	 * CREATE A CAT
	 * 
	 */
	@PostMapping("/cat")
	public ResponseEntity<Cat> createCat(@RequestBody Cat cat) {
	    try {
	    	Cat myCat = new Cat();
	    	myCat = catManagerService.createCat(cat);
	    	
	    	return new ResponseEntity<>(myCat, HttpStatus.CREATED);
	    } catch (Exception e) {
	    	return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
    }
	
	/*
	 * UPDATE A CAT
	 * 
	 */
	@PutMapping("/cat/{id}")
	public ResponseEntity<Cat> updateCat(@PathVariable("id") Long id
			, @RequestBody Cat cat) {
		
		Optional<Cat> myCat = catManagerService.findById(id);
		if(myCat.isPresent()) {
			/* Valid the gender */
			
			Cat foundCat = myCat.get();
			foundCat.setName(cat.getName());
			foundCat.setRace(cat.getRace());
			foundCat.setPrice(cat.getPrice());
			foundCat.setSex(cat.getSex());
			foundCat.setComment(cat.getComment());
			foundCat.setBirthDate(cat.getBirthDate());
			catManagerService.createCat(foundCat);
			return new ResponseEntity<>(foundCat, HttpStatus.OK);
		}
		
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
	
	/*
	 * DELETE A CAT
	 * 
	 */
	@DeleteMapping("/cat/{id}")
	public ResponseEntity<ResponseMessage> deleteCat(@PathVariable("id") Long id) {
		String message = "";
		try {
			Optional<Cat> myCat = catManagerService.findById(id);
			if (myCat.isPresent()) {
				catManagerService.deleteCat(myCat.get());
				message = "CAT data removed successfuly :-) " ;
		        
				return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ResponseMessage(message));
		        
			}
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage("CAT not found :( "));
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }
	
	/*
	 * 
	 * This function return a list of Cats by age
	 * the query param 'AGE' allow a filter of cats by age
	 * example : /cats-api/v1/cat/findByAge?age=<AGE>
	 * 
	 */
	@GetMapping("/cat/findByAge")
	public ResponseEntity<Map<String, Object>> catsByAge(@RequestParam(required = true) int age) { 
		Map<String, Object> response = new HashMap<>();
		
		/*
		 * Check if price valid
		 */
		if ( age< 0) {
			response.put("message", "Please enter a valid value for AGE parameter !");
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}else {
			try {
				List<Cat> cats = new ArrayList<Cat>();
				cats = catManagerService.findAll();
				cats = giveAgeToCats(cats); 
				
				cats = cats.stream().filter(a -> a.getAge() == age).collect(Collectors.toList());;				
				response.put("cats", cats);
			    			
				return new ResponseEntity<>(response, HttpStatus.OK);
			}catch(Exception e) {
				return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
	}
	
	/*
	 * 
	 * This function return a list of Cats by age
	 * the query param 'AGE' allow a filter of cats by age
	 * example : /cats-api/v1/cat/findByAge?age=<AGE>
	 * 
	 */
	@GetMapping("/cat/findByAge/olderThan")
	public ResponseEntity<Map<String, Object>> catsAgeOlder(@RequestParam(required = true) int age) { 
		Map<String, Object> response = new HashMap<>();
		
		/*
		 * Check if age valid
		 */
		if ( age < 0) {
			response.put("message", "Please enter a valid value for AGE parameter !");
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}else {
			try {
				List<Cat> cats = new ArrayList<Cat>();
				cats = catManagerService.findAll();
				cats = giveAgeToCats(cats); 
				
				cats = cats.stream().filter(a -> a.getAge() > age).collect(Collectors.toList());;				
				response.put("cats", cats);
			    			
				return new ResponseEntity<>(response, HttpStatus.OK);
			}catch(Exception e) {
				return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
	}
	
	/*
	 * 
	 * This function return a list of Cats by age
	 * the query param 'AGE' allow a filter of cats by age
	 * example : /cats-api/v1/cat/findByAge?age=<AGE>
	 * 
	 */
	@GetMapping("/cat/findByAge/youngerThan")
	public ResponseEntity<Map<String, Object>> catsAgeYounger(@RequestParam(required = true) int age) { 
		Map<String, Object> response = new HashMap<>();
		
		/*
		 * Check if age is valid
		 */
		if ( age < 0) {
			response.put("message", "Please enter a valid value for AGE parameter !");
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}else {
			try {
				List<Cat> cats = new ArrayList<Cat>();
				cats = catManagerService.findAll();
				cats = giveAgeToCats(cats); 
				
				cats = cats.stream().filter(a -> a.getAge() < age).collect(Collectors.toList());;				
				response.put("cats", cats);
			    			
				return new ResponseEntity<>(response, HttpStatus.OK);
			}catch(Exception e) {
				return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
	}
	
	/*
	 * 
	 * This function return a list of Cats by age
	 * the query param 'AGE' allow a filter of cats by age
	 * example : /cats-api/v1/cat/findByAge?age=<AGE>
	 * 
	 */
	@GetMapping("/cat/findByAge/between")
	public ResponseEntity<Map<String, Object>> catsAgeBetween(@RequestParam(required = true) int minAge
			, @RequestParam(required = true) int maxAge) { 
		Map<String, Object> response = new HashMap<>();
		
		/*
		 * Check if age is valid
		 */
		if ( minAge < 0 ) {
			response.put("message", "Please enter a valid value for minAGE parameter !");
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}else if ( maxAge < 0 ) {
			response.put("message", "Please enter a valid value for maxAGE parameter !");
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}else {
			try {
				List<Cat> cats = new ArrayList<Cat>();
				cats = catManagerService.findAll();
				cats = giveAgeToCats(cats); 
				
				cats = cats.stream().filter(a -> a.getAge() <= maxAge && a.getAge() >= minAge  ).collect(Collectors.toList());;				
				response.put("cats", cats);
			    			
				return new ResponseEntity<>(response, HttpStatus.OK);
			}catch(Exception e) {
				return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
	}
	
	/*
	 * This function will set the age for cats by calling setCatAge method
	 */
	public List<Cat> giveAgeToCats(List<Cat> cats){
		if (catsWithAge == null || catsWithAge.isEmpty()) {
			catsWithAge = new ArrayList<Cat>();
			catsWithAge = catManagerService.setCatAge(cats);
		}
		
		return catsWithAge ;
	}
}
