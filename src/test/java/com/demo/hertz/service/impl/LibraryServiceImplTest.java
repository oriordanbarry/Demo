package com.demo.hertz.service.impl;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.demo.hertz.fixtures.LibraryRecordFixture;
import com.demo.hertz.model.LibraryRecord;
import com.demo.hertz.model.Response;
import com.demo.hertz.model.enums.LoanStatus;
import com.demo.hertz.model.manager.CacheManager;
import com.demo.hertz.service.LibraryService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LibraryServiceImplTest {
	
	@Autowired
	private LibraryService libraryService;
	
	@Test
	public void addBookTest() {
		LibraryRecord record = LibraryRecordFixture.getLibraryrecord("1001-1001", 0);
		Response response = libraryService.addBook(record);
		assertEquals("200", response.getErrorCode());
		CacheManager.clearLibraryCache();
		assertEquals(0, CacheManager.getNUmberOfLibraryRecords());
		CacheManager.clearLoanCache();
		assertEquals(0, CacheManager.getNUmberOfLoanRecords());
	}	
	
	@Test
	public void deleteBookTest() {
		LibraryRecord record = LibraryRecordFixture.getLibraryrecord("1001-1001", 0);
		libraryService.addBook(record);
		Response response = libraryService.deleteBook(record.getId());
		assertEquals("200", response.getErrorCode());
		CacheManager.clearLibraryCache();
		assertEquals(0, CacheManager.getNUmberOfLibraryRecords());
		CacheManager.clearLoanCache();
		assertEquals(0, CacheManager.getNUmberOfLoanRecords());
	}	
	
	@Test
	public void loanBookTest() {
		LibraryRecord record = LibraryRecordFixture.getLibraryrecord("1001-1001", 0);
		libraryService.addBook(record);
		Response response = libraryService.loanBook(record);
		assertEquals("200", response.getErrorCode());
		CacheManager.clearLibraryCache();
		assertEquals(0, CacheManager.getNUmberOfLibraryRecords());
		CacheManager.clearLoanCache();
		assertEquals(0, CacheManager.getNUmberOfLoanRecords());
	}	
	
	@Test
	public void returnBookTest() {
		Response response = new Response();
		LibraryRecord record = LibraryRecordFixture.getLibraryrecord("1001-1001", 0);
		response = libraryService.addBook(record);
		assertEquals("200", response.getErrorCode());
		LibraryRecord checkOutRecord = LibraryRecordFixture.getLibraryrecord("1001-1001", 0);
		checkOutRecord.setStatus(LoanStatus.CHECKED_OUT.name());
		response = libraryService.loanBook(checkOutRecord);
		assertEquals("200", response.getErrorCode());
		LibraryRecord checkInRecord = LibraryRecordFixture.getLibraryrecord("1001-1001", 0);
		checkInRecord.setStatus(LoanStatus.CHECKED_IN.name());
		response = libraryService.returnBook(checkInRecord);
		assertEquals("200", response.getErrorCode());
	}	

}
