package com.demo.hertz.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LibraryRecord {

	private String id;
	
	private Book book;
	
	private User user;
	
	private String status;
}
