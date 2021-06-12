package com.cats.app.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;

import com.cats.app.entities.Cat;
import com.cats.app.entities.Gender;

public class CSVHelper {

	static String[] HEADER = { "Nom", "Sexe", "Né le", "Race", "Prix", "Commentaires" };
	
	public static boolean hasCSVFormat(MultipartFile file) {
	    if (Constants.TYPE.equals(file.getContentType())
	    		|| file.getContentType().equals("application/vnd.ms-excel")) {
	      return true;
	    }

	    return false;
	  }
	
	public static List<Cat> csvToListOfCats(InputStream is) {
		String sex = "";
		
	    try (
	    	
	    	BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
	        CSVParser csvParser = new CSVParser(fileReader,
	            CSVFormat.DEFAULT.withDelimiter(';').withQuote('"').withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim().withIgnoreSurroundingSpaces());) {
	    	
	    	List<Cat> catList = new ArrayList<>();

	    	Iterable<CSVRecord> csvRecords = csvParser.getRecords();

	    	for (CSVRecord csvRecord : csvRecords) {
	    		Cat cat = new Cat();
	    		sex = csvRecord.get(HEADER[1]);
	    	  
	    		cat.setName(csvRecord.get(HEADER[0]));
	    	  
	    		if (sex.equals(Gender.MALE.toString()))
	    			cat.setSex(Gender.MALE);  
	    		else
	    			cat.setSex(Gender.FEMALE);
	    		System.out.println(csvRecord.get(HEADER[4]));
	    	  
	    		cat.setBirthDate(Date.valueOf(csvRecord.get(HEADER[2]))); // to convert
	    		cat.setRace(csvRecord.get(HEADER[3]));
	    		cat.setPrice(Double.parseDouble(csvRecord.get(HEADER[4]).replace(",", ".")));
	    		cat.setComment(csvRecord.get(HEADER[5]));
	    	  
	    		catList.add(cat);
	      }

	      return catList;
	    } catch (IOException e) {
	      throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
	    }
	  }
}
