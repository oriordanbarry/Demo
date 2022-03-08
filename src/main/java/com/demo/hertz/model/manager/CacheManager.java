package com.demo.hertz.model.manager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.demo.hertz.model.LibraryRecord;
import com.demo.hertz.model.User;

@Component
public class CacheManager {

	public static Map<String, LibraryRecord> libraryRecords = new ConcurrentHashMap<>();

	public static Map<String, User> loanRecords = new ConcurrentHashMap<>();

	public static int getNUmberOfLibraryRecords() {
		return libraryRecords.size();
	}

	public static void clearLibraryCache() {
		libraryRecords.clear();

	}
	
	public static int getNUmberOfLoanRecords() {
		return loanRecords.size();
	}

	public static void clearLoanCache() {
		loanRecords.clear();
	}
}
