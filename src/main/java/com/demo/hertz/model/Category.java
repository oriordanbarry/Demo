package com.demo.hertz.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class Category {
	
	private String code;
	private String name;
	private String description;

}
