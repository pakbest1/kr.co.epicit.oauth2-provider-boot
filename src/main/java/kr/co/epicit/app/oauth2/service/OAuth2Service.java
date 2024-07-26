package kr.co.epicit.app.oauth2.service;

public interface OAuth2Service {

	boolean get     () throws Exception;
	boolean list    () throws Exception;
	boolean save    () throws Exception;
	boolean drop    () throws Exception;
	boolean commit  () throws Exception;
	boolean rollback() throws Exception;
}
