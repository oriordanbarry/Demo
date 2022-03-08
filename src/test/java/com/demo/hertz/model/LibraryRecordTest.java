package com.demo.hertz.model;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.demo.hertz.fixtures.LibraryRecordFixture;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LibraryRecordTest {
	
	@Test
	public void createLibraryRecord() {
		LibraryRecord record = LibraryRecordFixture.getLibraryrecord("test", 0);
		assertThat(record, instanceOf(LibraryRecord.class));
	}

}
