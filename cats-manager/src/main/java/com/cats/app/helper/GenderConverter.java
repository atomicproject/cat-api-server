package com.cats.app.helper;

import java.util.stream.Stream;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.cats.app.entities.Gender;

/*
 * THIS CLASS ALLOW SPECIAL CHARACTERS FOR ENUM VALUES
 * 
 * Author: T.A
 */

@Converter (autoApply = true)
public class GenderConverter implements AttributeConverter<Gender, String> {

	@Override
	public String convertToDatabaseColumn(Gender gender) {
		// TODO Auto-generated method stub
		if (gender == null)
			return null;
		
		return gender.getSex();
	}

	@Override
	public Gender convertToEntityAttribute(String sex) {
		// TODO Auto-generated method stub
		if (sex == null) {
            return null;
        }

        return Stream.of(Gender.values())
          .filter(g -> g.getSex().equals(sex))
          .findFirst()
          .orElseThrow(IllegalArgumentException::new);
	}

	
}
