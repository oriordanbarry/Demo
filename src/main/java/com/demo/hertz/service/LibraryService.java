package com.demo.hertz.service;

import org.springframework.stereotype.Service;

import com.demo.hertz.model.LibraryRecord;
import com.demo.hertz.model.Response;

@Service
public interface LibraryService {
	
	Response addBook(LibraryRecord record);
	
	Response deleteBook(String recordId);
	
	Response loanBook(LibraryRecord record);
	
	Response returnBook(LibraryRecord record);

}
