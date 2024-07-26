package kr.co.epicit.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.github.luben.zstd.ZstdInputStream;
import com.github.luben.zstd.ZstdOutputStream;

public class ZstdUtil {
	private ZstdUtil() {}

	// 데이터를 Zstd로 압축
	public static byte[] compress(byte[] data) throws IOException {
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ZstdOutputStream zos = new ZstdOutputStream(baos)) {
			zos.write(data);
			zos.close();
			return baos.toByteArray();
		}
	}

	// 데이터를 Zstd로 압축 해제
	public static byte[] decompress(byte[] compressedData) throws IOException {
		try (ByteArrayInputStream bais = new ByteArrayInputStream(compressedData);
			ZstdInputStream zis = new ZstdInputStream(bais);
			ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			byte[] buffer = new byte[1024];
			int len;
			while ((len = zis.read(buffer)) != -1) {
				baos.write(buffer, 0, len);
			}
			return baos.toByteArray();
		}
	}

}
