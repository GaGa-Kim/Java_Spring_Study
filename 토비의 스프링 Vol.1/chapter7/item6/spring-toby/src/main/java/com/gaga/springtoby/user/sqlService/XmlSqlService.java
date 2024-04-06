package com.gaga.springtoby.user.sqlService;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.gaga.springtoby.user.dao.UserDao;
import com.gaga.springtoby.user.sqlService.jaxb.SqlType;
import com.gaga.springtoby.user.sqlService.jaxb.Sqlmap;

/**
 * XmlSqlService 클래스
 */
public class XmlSqlService implements SqlService, SqlRegistry, SqlReader {
	// 의존 오브젝트를 DI 받을 수 있도록 인터페이스 타입의 프로퍼티를 선언해둔다.
	private SqlReader sqlReader;
	private SqlRegistry sqlRegistry;

	public void setSqlReader(SqlReader sqlReader) {
		this.sqlReader = sqlReader;
	}

	public void setSqlRegistry(SqlRegistry sqlRegistry) {
		this.sqlRegistry = sqlRegistry;
	}

	/**
	 * SqlService 인터페이스 구현
	 */
	@PostConstruct
	public void loadSql() {
		// SqlReader에게 SqlRegistry를 전달하면서 SQL을 읽어서 저장해두도록 요청한다.
		this.sqlReader.read(this.sqlRegistry);
	}

	public String getSql(String key) throws SqlRetrievalFailureException {
		try {
			// SqlRegistry 타입 오브젝트에게 요청해서 SQL을 가져오게 한다.
			return this.sqlRegistry.findSql(key);
		} catch (SqlNotFoundException e) {
			throw new SqlRetrievalFailureException(e.getMessage());
		}
	}

	/**
	 * SqlReader 인터페이스 구현
	 */
	// sqlmapFile은 SqlReader 구현의 일부가 된다. 따라서 SqlReader 구현 메소드를 통하지 않고는 접근하면 안 된다.
	private String sqlmapFile;

	public void setSqlmapFile(String sqlmapFile) {
		this.sqlmapFile = sqlmapFile;
	}

	// loadSql()에 있던 코드를 SqlReader 메소드로 가져온다.
	// 초기화를 위해 무엇을 할 것인가와 SQL을 어떻게 읽는지를 분리한다.
	public void read(SqlRegistry sqlRegistry) {
		String contextPath = Sqlmap.class.getPackage().getName();
		try {
			JAXBContext context = JAXBContext.newInstance(contextPath);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			InputStream is = UserDao.class.getResourceAsStream(this.sqlmapFile);
			Sqlmap sqlmap = (Sqlmap)unmarshaller.unmarshal(is);
			for (SqlType sql : sqlmap.getSql()) {
				// SQL 저장 로직 구현에 독립적인 인터페이스 메소드를 통해 읽어들인 SQL과 키를 전달한다.
				sqlRegistry.registerSql(sql.getKey(), sql.getValue());
			}
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 *  SqlRegistry 인터페이스 구현
	 */
	// sqlMap은 SqlRegistry 구현의 일부가 되므로 외부에서 직접 접근할 수 없다.
	private Map<String, String> sqlMap = new HashMap<String, String>();

	public String findSql(String key) throws SqlNotFoundException {
		String sql = sqlMap.get(key);
		if (sql == null)
			throw new SqlNotFoundException(key + "에 대한 SQL을 찾을 수 없습니다");
		else
			return sql;
	}

	// HashMap이라는 저장소를 사용하는 구체적인 구현 방법에서 독립될 수 있도록 인터페이스의 메소드로 접근하게 해준다.
	public void registerSql(String key, String sql) {
		sqlMap.put(key, sql);
	}
}
