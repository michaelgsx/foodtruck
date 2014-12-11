package uber.foodtruck.definitions;

import junit.framework.Assert;

import org.testng.annotations.Test;

public class TestLongLat {
	@Test
	public void testLongLat() {
		LongLat ll = new LongLat(1.1, 2.2);
		
		Assert.assertTrue(ll.toString().contains("1.1"));
	}
}
