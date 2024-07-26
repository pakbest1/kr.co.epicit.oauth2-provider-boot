package kr.co.epicit.app.transfer.cafe24.repo;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;

@SuppressWarnings("unused")
@Repository
public class Cafe24OAuth2Repository {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private ObjectMapper objectMapper = new ObjectMapper();

	private String cafe24_oauth2_path = "C:/jdevworks/ws/ws_egov4.1/oauth2.0-demo-host/dataset/cafe24.oauth2.json";
	private File cafe24_oauth2_file;
	private Map<String, Object> cafe24_oauth2_repo = new HashMap<String, Object>();
	private Map<String, String> cafe24_oauth2_config = new HashMap<String, String>();
	private Map<String, String> cafe24_oauth2_accesstoken = new HashMap<String, String>();

	@PostConstruct
	public void postConstruct() {
		logger.debug(this.getClass().getSimpleName()+".postConstruct() start");

		load();


		logger.debug(this.getClass().getSimpleName()+".postConstruct() finish");
	}

	@SuppressWarnings("unchecked")
	public Cafe24OAuth2Repository load() {
		try {
			loadConfigFile();
			if (cafe24_oauth2_file.exists()) {
				cafe24_oauth2_repo = objectMapper.readValue(cafe24_oauth2_file, cafe24_oauth2_repo.getClass());
			}

		} catch (Exception e) {
			logger.error ("exception >> ", e);
		}

		return this;
	}

	public Cafe24OAuth2Repository commit() {



		return this;
	}



	private File loadConfigFile() {
		if ( cafe24_oauth2_file == null) { cafe24_oauth2_file = new File(cafe24_oauth2_path); }
		if (!cafe24_oauth2_file.exists()) {
			try {
				cafe24_oauth2_file.createNewFile();
			} catch (Exception e) {
				logger.error ("exception >> cafe24_oauth2_file.getAbsolutePath()\n["+ cafe24_oauth2_file.getAbsolutePath() +"]\n", e);
			}
		}

		return cafe24_oauth2_file;
	}


}
