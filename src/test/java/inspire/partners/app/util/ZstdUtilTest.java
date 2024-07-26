package inspire.partners.app.util;

import static org.junit.Assert.assertArrayEquals;

import java.io.IOException;

import org.junit.Test;

import kr.co.epicit.util.ZstdUtil;

public class ZstdUtilTest {

	@Test
	public void testCompressionDecompression() throws IOException {
		String originalString = "Hello, Zstd!";
		byte[] originalData = originalString.getBytes();

		byte[] compressedData = ZstdUtil.compress(originalData);
		byte[] decompressedData = ZstdUtil.decompress(compressedData);

		assertArrayEquals(originalData, decompressedData);
	}

}
