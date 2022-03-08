package com.demo.hertz.model.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.demo.hertz.model.LibraryRecord;
import com.demo.hertz.model.Response;
import com.demo.hertz.service.LibraryCacheManagerService;
import com.demo.hertz.service.LibraryService;

@Component
public class LibraryServiceImpl implements LibraryService{
	private static Logger log = LoggerFactory.getLogger(LibraryCacheManagerServiceImpl.class);

	
	@Autowired
	private LibraryCacheManagerService libraryCacheManagerService;

	@Override
	public Response addBook(LibraryRecord record) {
		log.info("Record added to library");
		return libraryCacheManagerService.addLibraryRecord(record);
	}
	
	@Override
	public Response deleteBook(String recordId) {
		log.info("Record deleted from library");
		return libraryCacheManagerService.deleteLibraryRecod(recordId);
	}

	@Override
	public Response loanBook(LibraryRecord record) {
		Response response = libraryCacheManagerService.addUserLoanRecord(record.getUser());
		if (response.getErrorCode().equals("200")) {
			response = libraryCacheManagerService.updateLibraryRecord(record);
		}
		return response;
	}

	@Override
	public Response returnBook(LibraryRecord record) {
		Response response = libraryCacheManagerService.deleteUserLoanRecord(record.getUser().getId());
		if (response.getErrorCode().equals("206")) {
			response = libraryCacheManagerService.updateLibraryRecord(record);
		}
		return response;
	}

}
