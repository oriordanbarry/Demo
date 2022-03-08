package com.demo.hertz.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class Response {
	
	String status;
	String errorCode;
	String description;

}
