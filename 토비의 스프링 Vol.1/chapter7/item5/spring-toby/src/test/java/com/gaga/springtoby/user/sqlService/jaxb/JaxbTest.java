package com.gaga.springtoby.user.sqlService.jaxb;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.junit.jupiter.api.Test;

/**
 * JAXB 학습 테스트
 */
public class JaxbTest {
	@Test
	public void readSqlmap() throws JAXBException {
		// 바인드용 클래스들 위치를 가지고 JAXB 컨텍스트를 만든다.
		String contextPath = Sqlmap.class.getPackage().getName();
		JAXBContext context = JAXBContext.newInstance(contextPath);

		// 언마샬러 생성
		Unmarshaller unmarshaller = context.createUnmarshaller();

		// 언마샬을 하면 매핑된 오브젝트 트리의 루트인 Sqlmap을 돌려준다.
		// 리소스 폴더에 있는 XML 파일을 사용한다.
		Sqlmap sqlmap = (Sqlmap)unmarshaller.unmarshal(getClass().getResourceAsStream("/sqlmap.xml"));

		List<SqlType> sqlList = sqlmap.getSql();

		// List에 담겨 있는 Sql 오브젝트를 가져와 XML 문서와 같은 정보를 갖고 있는지 확인한다.
		assertThat(sqlList.size(), is(3));
		assertThat(sqlList.get(0).getKey(), is("add"));
		assertThat(sqlList.get(0).getValue(), is("insert"));
		assertThat(sqlList.get(1).getKey(), is("get"));
		assertThat(sqlList.get(1).getValue(), is("select"));
		assertThat(sqlList.get(2).getKey(), is("delete"));
		assertThat(sqlList.get(2).getValue(), is("delete"));
	}
}
