package com.demo.hertz.fixtures;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;

import com.demo.hertz.model.Book;
import com.demo.hertz.model.Category;
import com.demo.hertz.model.LibraryRecord;
import com.demo.hertz.model.User;
import com.demo.hertz.model.enums.LoanStatus;

public class LibraryRecordFixture {
	
	
	public static LibraryRecord getLibraryrecord(String id, int days) {
		
		LibraryRecord record = new LibraryRecord();
		record.setId(id);
		List<Category> categfories = new ArrayList<>(); 
		categfories.add(new Category("1001", "Fiction", "total fiction"));
		categfories.add(new Category("1002", "Science", "science fiction"));
		Date reservationDate = new Date();
		DateTime dueDate = new DateTime(reservationDate);
		Book book = new Book(id, "Book Title", "Book Author", categfories, reservationDate, dueDate.plusDays(days));
		record.setBook(book);
		User user = new User("userid", "user name", "user email", 1, 0);
		record.setUser(user);
		record.setStatus(LoanStatus.CHECKED_IN.name());
		return record;
	}
}
