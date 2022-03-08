package com.demo.hertz.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.demo.hertz.fixtures.LibraryRecordFixture;
import com.demo.hertz.model.LibraryRecord;
import com.demo.hertz.model.Response;
import com.demo.hertz.model.User;
import com.demo.hertz.model.enums.LoanStatus;
import com.demo.hertz.model.manager.CacheManager;
import com.demo.hertz.service.LibraryCacheManagerService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LibraryCacheManagerServiceImplTest {
	
	private final String ISBN = "1001-1001-1";
	private final String USER_ID = "userId1";
	private final String USER_ID_2 = "userId2";
	
	@Autowired
	private LibraryCacheManagerService libraryCacheManagerService;
	
	
	@Test
	public void AddLibraryRecordTest() {
		addLibraryRecordToCache();
		assertEquals(1, CacheManager.getNUmberOfLibraryRecords());
		CacheManager.clearLibraryCache();
		assertEquals(0, CacheManager.getNUmberOfLibraryRecords());
	}
	
	@Test
	public void getLibraryRecordTest() {
		addLibraryRecordToCache();
		LibraryRecord record = libraryCacheManagerService.getLibraryRecord(ISBN);
		assertNotNull(record);
		assertEquals(ISBN, record.getId());
		CacheManager.clearLibraryCache();
		assertEquals(0, CacheManager.getNUmberOfLibraryRecords());
	}
	
	@Test
	public void deleteLibraryRecordTest() {
		addLibraryRecordToCache();
		Response response = libraryCacheManagerService.deleteLibraryRecod(ISBN);
		assertEquals("200", response.getErrorCode());
		assertEquals(0, CacheManager.getNUmberOfLibraryRecords());
		CacheManager.clearLibraryCache();
		assertEquals(0, CacheManager.getNUmberOfLibraryRecords());
	}
	
	@Test
	public void updateLibraryRecordTest() {
		addLibraryRecordToCache();
		LibraryRecord checkOutRecordRecord = LibraryRecordFixture.getLibraryrecord(ISBN, 0);
		checkOutRecordRecord.setStatus(LoanStatus.CHECKED_OUT.name());
		libraryCacheManagerService.addUserLoanRecord(checkOutRecordRecord.getUser());
		Response response = libraryCacheManagerService.updateLibraryRecord(checkOutRecordRecord);
		assertEquals("200", response.getErrorCode());
		assertEquals(1, CacheManager.getNUmberOfLibraryRecords());
		LibraryRecord cachedrecord = libraryCacheManagerService.getLibraryRecord(ISBN);
		assertEquals(LoanStatus.CHECKED_OUT.name(), cachedrecord.getStatus());
		LibraryRecord checkOutRecordRecord2 = LibraryRecordFixture.getLibraryrecord(ISBN, 0);
		checkOutRecordRecord2.setStatus(LoanStatus.CHECKED_OUT.name());
		Response response2 = libraryCacheManagerService.updateLibraryRecord(checkOutRecordRecord2);
		assertEquals("207", response2.getErrorCode());
		assertEquals(1, CacheManager.getNUmberOfLibraryRecords());
		CacheManager.clearLibraryCache();
		assertEquals(0, CacheManager.getNUmberOfLibraryRecords());
		CacheManager.clearLoanCache();
		assertEquals(0, CacheManager.getNUmberOfLoanRecords());
	}
	
	@Test
	public void addSingleUserLoanRecordTest() {
		addLoanRecordToCache(USER_ID);
		assertEquals(1,CacheManager.getNUmberOfLoanRecords());
		CacheManager.clearLoanCache();
		assertEquals(0, CacheManager.getNUmberOfLoanRecords());
	}
	
	@Test
	public void addMultipleUserLoanRecordTest() {
		addLoanRecordToCache(USER_ID);
		User cachedUser = libraryCacheManagerService.getUserLoanRecord(USER_ID);
		assertNotNull(cachedUser);
		assertEquals(1, cachedUser.getNumberOfBooks());
		addLoanRecordToCache(USER_ID_2);
		User cachedUser2 = libraryCacheManagerService.getUserLoanRecord(USER_ID_2);
		assertNotNull(cachedUser2);
		assertEquals(1, cachedUser2.getNumberOfBooks());
		assertEquals(2, CacheManager.getNUmberOfLoanRecords());
		CacheManager.clearLoanCache();
		assertEquals(0, CacheManager.getNUmberOfLoanRecords());
	}
	
	@Test
	public void CheckOutMultipleBooksForSameUserTest() {
		addLoanRecordToCache(USER_ID);
		User existingUser = libraryCacheManagerService.getUserLoanRecord(USER_ID);
		assertNotNull(existingUser);
		assertEquals(1, existingUser.getNumberOfBooks());
		libraryCacheManagerService.addUserLoanRecord(existingUser);
		existingUser = libraryCacheManagerService.getUserLoanRecord(USER_ID);
		assertEquals(2, existingUser.getNumberOfBooks());
		libraryCacheManagerService.addUserLoanRecord(existingUser);
		existingUser = libraryCacheManagerService.getUserLoanRecord(USER_ID);
		assertEquals(3, existingUser.getNumberOfBooks());
		Response response = libraryCacheManagerService.addUserLoanRecord(existingUser);
		assertEquals("201", response.getErrorCode()); 
		CacheManager.clearLoanCache();
		assertEquals(0, CacheManager.getNUmberOfLoanRecords());
	}
	
	@Test
	public void deleteNonExistantUserTest() {
		Response response = libraryCacheManagerService.deleteUserLoanRecord(USER_ID);
		assertEquals("203", response.getErrorCode());
		CacheManager.clearLibraryCache();
		assertEquals(0, CacheManager.getNUmberOfLoanRecords());
	}
	
	@Test
	public void CheckInSingleBookForUserTest() {
		addLoanRecordToCache(USER_ID);
		User existingUser = libraryCacheManagerService.getUserLoanRecord(USER_ID);
		assertNotNull(existingUser);
		assertEquals(1, existingUser.getNumberOfBooks());
		Response response = libraryCacheManagerService.deleteUserLoanRecord(USER_ID);
		assertEquals("206", response.getErrorCode()); 
		assertEquals(0, CacheManager.getNUmberOfLoanRecords());
		CacheManager.clearLoanCache();
		assertEquals(0, CacheManager.getNUmberOfLoanRecords());
	}
	
	@Test
	public void CheckInMultipleBooksForsameUser() {
		addLoanRecordToCache(USER_ID);
		User existingUser = libraryCacheManagerService.getUserLoanRecord(USER_ID);
		assertNotNull(existingUser);
		assertEquals(1, existingUser.getNumberOfBooks());
		libraryCacheManagerService.addUserLoanRecord(existingUser);
		existingUser = libraryCacheManagerService.getUserLoanRecord(USER_ID);
		assertEquals(2, existingUser.getNumberOfBooks());
		libraryCacheManagerService.addUserLoanRecord(existingUser);
		existingUser = libraryCacheManagerService.getUserLoanRecord(USER_ID);
		assertEquals(3, existingUser.getNumberOfBooks());
		CacheManager.clearLoanCache();
		assertEquals(0, CacheManager.getNUmberOfLoanRecords());
	}
	
	private void addLibraryRecordToCache() {
		LibraryRecord record = LibraryRecordFixture.getLibraryrecord(ISBN, 0);
		Response response = libraryCacheManagerService.addLibraryRecord(record);
		assertEquals("200", response.getErrorCode());
		assertEquals(1, CacheManager.getNUmberOfLibraryRecords());
	}
	

	private void addLoanRecordToCache(String userId) {
		User user = createUser(userId);
		libraryCacheManagerService.addUserLoanRecord(user);
	}
	
	private User createUser(String userId) {
		User user = new User();
		user.setId(userId);
		user.setName("user name");
		user.setEmail("useremail@gmail.com");
		user.setNumberOfBooks(1);
		return user;
	}
}
