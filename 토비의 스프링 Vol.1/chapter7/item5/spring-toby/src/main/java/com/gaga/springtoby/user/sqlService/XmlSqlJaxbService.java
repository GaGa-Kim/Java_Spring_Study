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
 * 생성자 초기화 방법을 사용하는 XmlSqlJaxbService 클래스
 */
public class XmlSqlJaxbService implements SqlService {

	// SQL 맵 파일 이름 프로퍼티
	private String sqlmapFile;

	public void setSqlmapFile(String sqlmapFile) {
		this.sqlmapFile = sqlmapFile;
	}

	// 읽어온 SQL을 저장하는 맵
	private Map<String, String> sqlMap = new HashMap<String, String>();

	// 스프링이 오브젝트를 만드는 시점에서 SQL을 읽어오도록 생성자를 이용한다.
	public XmlSqlJaxbService() {
	}

	// 생성자 대신 사용할 초기화 메소드
	@PostConstruct
	public void loadSql() {
		String contextPath = Sqlmap.class.getPackage().getName();
		try {
			JAXBContext context = JAXBContext.newInstance(contextPath);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			// 리소스 폴더의 usersqlmap.xml 파일을 변환한다.
			// 프로퍼티 설정을 통해 제공받은 파일 이름을 사용한다.
			InputStream is = UserDao.class.getResourceAsStream(this.sqlmapFile);
			Sqlmap sqlmap = (Sqlmap)unmarshaller.unmarshal(is);

			// 읽어온 SQL을 맵으로 저장해둔다.
			for (SqlType sql : sqlmap.getSql()) {
				sqlMap.put(sql.getKey(), sql.getValue());
			}
		} catch (JAXBException e) {
			// JAXBException은 복구 불가능한 예외다. 불필요한 throws를 피하도록 런타임 예외로 포장해서 던진다.
			throw new RuntimeException(e);
		}
	}

	public String getSql(String key) throws SqlRetrievalFailureException {
		String sql = sqlMap.get(key);
		if (sql == null)
			throw new SqlRetrievalFailureException(key + "를 이용해서 SQL을 찾을 수 없습니다");
		else
			return sql;
	}
}
