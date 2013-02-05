import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.Before;


public class FutureMeetingImplTest {
	public FutureMeetingImpl demo;

	@Before
	public void buildUp() {
		// add code from MeetingImplTest
		
	}
	
	@Test
	public void testGetId() {
		int output = demo.getId();
		int expected = 123456789;
		assertEquals(output, expected);
	}

	@Test
	public void testGetDate() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetContacts() {
		fail("Not yet implemented");
	}

}
