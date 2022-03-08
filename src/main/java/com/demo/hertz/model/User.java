package com.demo.hertz.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class User {
	
	private String id;
	
	private String name;
	
	private String email;
	
	private int numberOfBooks;
	
	private int numberOfOverDueBooks;

}
