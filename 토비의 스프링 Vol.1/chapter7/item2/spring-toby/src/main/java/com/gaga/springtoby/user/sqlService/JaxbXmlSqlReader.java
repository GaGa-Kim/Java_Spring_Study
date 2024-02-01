package com.gaga.springtoby.user.sqlService;

import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.gaga.springtoby.user.dao.UserDao;
import com.gaga.springtoby.user.sqlService.jaxb.SqlType;
import com.gaga.springtoby.user.sqlService.jaxb.Sqlmap;

/**
 * JAXB를 사용하는 SqlReader 클래스
 */
public class JaxbXmlSqlReader implements SqlReader {
	private static final String DEFAULT_SQLMAP_FILE = "/usersqlmap.xml";

	// sqlmapFile은 SqlReader 구현의 일부가 된다. 따라서 SqlReader 구현 메소드를 통하지 않고는 접근하면 안 된다.
	private String sqlmapFile = DEFAULT_SQLMAP_FILE;

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
}
