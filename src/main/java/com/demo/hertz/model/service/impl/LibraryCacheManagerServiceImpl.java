package com.demo.hertz.model.service.impl;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.demo.hertz.model.LibraryRecord;
import com.demo.hertz.model.Response;
import com.demo.hertz.model.User;
import com.demo.hertz.model.enums.ErrorResponseStatus;
import com.demo.hertz.model.enums.LoanStatus;
import com.demo.hertz.model.manager.CacheManager;
import com.demo.hertz.service.LibraryCacheManagerService;

@Component
public class LibraryCacheManagerServiceImpl implements LibraryCacheManagerService {
	private static Logger log = LoggerFactory.getLogger(LibraryCacheManagerServiceImpl.class);
	private final static int LENDING_PERIOD = 7;

	@Override
	public Response addLibraryRecord(LibraryRecord record) {
		CacheManager.libraryRecords.put(record.getBook().getIsbn(), record);
		Response response = new Response();
		response.setStatus(ErrorResponseStatus.SUCCESS.name());
		response.setErrorCode("200");
		response.setDescription("Completed Successfully");
		log.info("####  Record added to Library Cache" + record.getBook().getIsbn());
		return response;
	}

	@Override
	public Response deleteLibraryRecod(String id) {
		Response response = new Response();
		if (CacheManager.libraryRecords.containsKey(id)) {
			CacheManager.libraryRecords.remove(id);
			log.info("####  Record removed from Library Cache" + id);
			response.setStatus(ErrorResponseStatus.SUCCESS.name());
			response.setErrorCode("200");
			response.setDescription("Completed Successfully");
			return response;
		}
		response.setStatus(ErrorResponseStatus.FAILED.name());
		response.setErrorCode("209");
		response.setDescription("No record found to delete");
		return response;
	}
	
	@Override
	public Response updateLibraryRecord(LibraryRecord record) {
		Response response = new Response();
		DateTime now = new DateTime();
		DateTime duedate = new DateTime();
		User cachedUser = CacheManager.loanRecords.get(record.getUser().getId());
		if (CacheManager.libraryRecords.containsKey(record.getBook().getIsbn())) {
			LibraryRecord cacheRecord = CacheManager.libraryRecords.get(record.getBook().getIsbn());
			if (cacheRecord.getStatus().equals(LoanStatus.CHECKED_OUT.name())
					&& record.getStatus().equals(LoanStatus.CHECKED_OUT.name())) {
				response.setStatus(ErrorResponseStatus.FAILED.name());
				response.setErrorCode("207");
				response.setDescription("Record is already checked out");
			} else if (cacheRecord.getBook().getDueDate().isAfter(now.plusDays(LENDING_PERIOD))
					|| (cachedUser != null && cachedUser.getNumberOfOverDueBooks() > 0)) {
				updateUserLoanRecordOfOverdueBooks(record.getUser());
				response.setStatus(ErrorResponseStatus.FAILED.name());
				response.setErrorCode("208");
				response.setDescription("User has overdue books");
			} else {
				if (record.getStatus().equals(LoanStatus.CHECKED_OUT.name())) {
					record.getBook().setDueDate(duedate.plusDays(7));
				} else {
					record.getBook().setDueDate(duedate);
				}
				CacheManager.libraryRecords.put(record.getBook().getIsbn(), record);
				response.setStatus(ErrorResponseStatus.SUCCESS.name());
				response.setErrorCode("200");
				response.setDescription("Completed Successfully");
			}
		}
		return response;
	}

	@Override
	public LibraryRecord getLibraryRecord(String id) {
		return CacheManager.libraryRecords.get(id);
	}

	@Override
	public Response addUserLoanRecord(User user) {
		int countOfBooksOnLoan = 1;
		Response response = new Response();
		if (!CacheManager.loanRecords.containsKey(user.getId())) {
			CacheManager.loanRecords.put(user.getId(), user);
			response.setStatus(ErrorResponseStatus.SUCCESS.name());
			response.setErrorCode("200");
			response.setDescription("Completed Successfully");
		} else if (CacheManager.loanRecords.containsKey(user.getId())) {
			if (user.getNumberOfBooks() <= 2) {
				countOfBooksOnLoan = user.getNumberOfBooks() + 1;
				user.setNumberOfBooks(countOfBooksOnLoan);
				CacheManager.loanRecords.put(user.getId(), user);
				response.setStatus(ErrorResponseStatus.SUCCESS.name());
				response.setErrorCode("200");
				response.setDescription("Completed Successfully");
			} else {
				response.setStatus(ErrorResponseStatus.FAILED.name());
				response.setErrorCode("201");
				response.setDescription("Too Many books checked out");
			}
		} else {
			response.setStatus(ErrorResponseStatus.FAILED.name());
			response.setErrorCode("202");
			response.setDescription("No record found");
		}
		return response;
	}

	@Override
	public User getUserLoanRecord(String userId) {
		return CacheManager.loanRecords.get(userId);
	}

	@Override
	public Response deleteUserLoanRecord(String userId) {
		Response response = new Response();
		User user = CacheManager.loanRecords.get(userId);
		if (!CacheManager.loanRecords.containsKey(userId) && user == null) {
			response.setStatus(ErrorResponseStatus.FAILED.name());
			response.setErrorCode("203");
			response.setDescription("User has no books checked out");
			log.info("#### No User record found");
		} else if (CacheManager.loanRecords.containsKey(userId)) {
			int numberOfBooksCheckedOut = user.getNumberOfBooks();
			if (numberOfBooksCheckedOut <= 3 && numberOfBooksCheckedOut > 0) {
				numberOfBooksCheckedOut = numberOfBooksCheckedOut - 1;
				user.setNumberOfBooks(numberOfBooksCheckedOut);
				if (numberOfBooksCheckedOut == 0) {
					response = removeUserLoanRecord(userId);
				} else {
					CacheManager.loanRecords.put(user.getId(), user);
					response.setStatus(ErrorResponseStatus.SUCCESS.name());
					response.setErrorCode("205");
					response.setDescription("User Check in successful");
					log.info("####  User Record removed from Loan cache " + user.getId());
				}
			}
		} else {
			response = removeUserLoanRecord(userId);
		}
		return response;
	}

	private Response removeUserLoanRecord(String userId) {
		Response response = new Response();
		CacheManager.loanRecords.remove(userId);
		response.setStatus(ErrorResponseStatus.SUCCESS.name());
		response.setErrorCode("206");
		response.setDescription("User Checked in all books");
		log.info("####  User Record removed from Loan cache " + userId);
		return response;
	}
	
	private void updateUserLoanRecordOfOverdueBooks(User user) {
		if (CacheManager.loanRecords.containsKey(user.getId())) {
			User cachedUser = CacheManager.loanRecords.get(user.getId());
			user.setNumberOfOverDueBooks(cachedUser.getNumberOfOverDueBooks() + 1);
			CacheManager.loanRecords.put(user.getId(), user);
		}
	}
}
