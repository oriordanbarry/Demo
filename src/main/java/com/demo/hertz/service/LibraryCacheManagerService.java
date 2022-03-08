package com.demo.hertz.service;

import org.springframework.stereotype.Service;

import com.demo.hertz.model.LibraryRecord;
import com.demo.hertz.model.Response;
import com.demo.hertz.model.User;

@Service
public interface LibraryCacheManagerService {
	
	Response addLibraryRecord(LibraryRecord record);
	
	Response deleteLibraryRecod(String id);
	
	Response updateLibraryRecord(LibraryRecord record);
	
	LibraryRecord getLibraryRecord(String id);
	
	Response addUserLoanRecord(User user);
	
	Response deleteUserLoanRecord(String user);
	
	User getUserLoanRecord(String userId);

}
