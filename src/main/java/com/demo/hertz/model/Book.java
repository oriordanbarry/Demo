package com.demo.hertz.model;


import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class Book {
	
	private String isbn;
	
	private String title;
	
	private String author;
	
	private List<Category> category;
	
	private Date reservationDate;
	
	private DateTime dueDate;

}
