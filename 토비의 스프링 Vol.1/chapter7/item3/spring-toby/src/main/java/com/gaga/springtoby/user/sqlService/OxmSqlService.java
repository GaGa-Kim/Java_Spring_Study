package com.gaga.springtoby.user.sqlService;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.oxm.Unmarshaller;

import com.gaga.springtoby.user.dao.UserDao;
import com.gaga.springtoby.user.sqlService.jaxb.SqlType;
import com.gaga.springtoby.user.sqlService.jaxb.Sqlmap;

/**
 * OxmSqlService 클래스
 */
public class OxmSqlService implements SqlService {
	// SqlService의 실제 구현 부분을 위임할 대상인 BaseSqlService를 인스턴스 변수로 정의해둔다.
	private final BaseSqlService baseSqlService = new BaseSqlService();
	// final이므로 변경이 불가능하다. OxmSqlService와 OxmSqlReader는 강하게 결합돼서 하나의 빈으로 등록되고 한 번에 설정할 수 있다.
	private final OxmSqlReader oxmSqlReader = new OxmSqlReader();

	// OxmSqlReader와 달리 단지 디폴트 오브젝트로 만들어진 프로퍼티다.
	private SqlRegistry sqlRegistry = new HashMapSqlRegistry();

	// 필요에 따라 DI를 통해 교체 가능하다.
	public void setSqlRegistry(SqlRegistry sqlRegistry) {
		this.sqlRegistry = sqlRegistry;
	}

	// OxmSqlService의 공개된 프로퍼티를 통해 DI 받은 것을 그대로 멤버 클래스의 오브젝트에 전달한다.
	// 이들은 단일 빈 설정구조를 위한 창구 역할을 할 뿐이다.
	public void setUnmarshaller(Unmarshaller unmarshaller) {
		this.oxmSqlReader.setUnmarshaller(unmarshaller);
	}

	/*
	public void setSqlmapFile(String sqlmapFile) {
		this.oxmSqlReader.setSqlmapFile(sqlmapFile);
	} */

	// 이름과 타입을 모두 변경한다. 큰 변화이지만 그만큼 기능을 확장하고 유연을 얻는 것이니 과감하게 변경한다.
	public void setSqlmap(Resource sqlmap) {
		this.oxmSqlReader.setSqlmap(sqlmap);
	}

	@PostConstruct
	public void loadSql() {
		// this.oxmSqlReader.read(this.sqlRegistry);
		// OxmSqlService의 프로퍼티를 통해서 초기화된 SqlReader와 SqlRegistry를 실제 작업을 위임할 대상인 baseSqlService에 주입한다.
		this.baseSqlService.setSqlReader(this.oxmSqlReader);
		this.baseSqlService.setSqlRegistry(this.sqlRegistry);
		// SQL을 등록하는 초기화 작업을 baseSqlService에 위임한다.
		this.baseSqlService.loadSql();
	}

	public String getSql(String key) throws SqlRetrievalFailureException {
		/*
		try {
			// SqlRegistry 타입 오브젝트에게 요청해서 SQL을 가져오게 한다.
			return this.sqlRegistry.findSql(key);
		} catch (SqlNotFoundException e) {
			throw new SqlRetrievalFailureException(e.getMessage());
		} */
		// SQL을 찾아오는 작업도 baseSqlService에 위임한다.
		return this.baseSqlService.getSql(key);
	}

	// private 맴버 클래스로 정의한다. 톱레벨 클래스인 OxmSqlService만이 사용할 수 있다.
	private class OxmSqlReader implements SqlReader {
		private Unmarshaller unmarshaller;
		/* private static final String DEFAULT_SQLMAP_FILE = "/usersqlmap.xml";
		   private String sqlmapFile = DEFAULT_SQLMAP_FILE; */
		// 디폴트 파일은 기존과 같지만 이제 Resource 구현 클래스인 ClassPathResource를 이용한다.
		private Resource sqlmap = new ClassPathResource("/usersqlmap.xml", UserDao.class);

		public void setUnmarshaller(Unmarshaller unmarshaller) {
			this.unmarshaller = unmarshaller;
		}

		/*
		public void setSqlmapFile(String sqlmapFile) {
			this.sqlmapFile = sqlmapFile;
		} */

		public void setSqlmap(Resource sqlmap) {
			this.sqlmap = sqlmap;
		}

		// 초기화를 위해 무엇을 할 것인가와 SQL을 어떻게 읽는지를 분리한다.
		public void read(SqlRegistry sqlRegistry) {
			try {
				// Source source = new StreamSource(getClass().getResourceAsStream(this.sqlmapFile));
				Source source = new StreamSource(sqlmap.getInputStream());
				// OxmSqlService를 통해 전달받은 OXM 인터페이스 구현 오브젝트를 가지고 언마샬링 작업을 수행한다.
				Sqlmap sqlmap = (Sqlmap)this.unmarshaller.unmarshal(source);
				for (SqlType sql : sqlmap.getSql()) {
					// SQL 저장 로직 구현에 독립적인 인터페이스 메소드를 통해 읽어들인 SQL과 키를 전달한다.
					sqlRegistry.registerSql(sql.getKey(), sql.getValue());
				}
			} catch (IOException e) {
				// throw new IllegalArgumentException(this.sqlmapFile + "을 가져올 수 없습니다", e);
				throw new IllegalArgumentException(this.sqlmap.getFilename() + "을 가져올 수 없습니다", e);
			}
		}
	}
}
