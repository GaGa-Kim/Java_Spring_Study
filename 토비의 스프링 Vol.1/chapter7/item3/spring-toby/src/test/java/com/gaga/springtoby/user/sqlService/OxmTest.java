package com.gaga.springtoby.user.sqlService;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.Unmarshaller;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gaga.springtoby.user.sqlService.jaxb.SqlType;
import com.gaga.springtoby.user.sqlService.jaxb.Sqlmap;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/OxmTest-context.xml")
// @ContextConfiguration(locations = "/mapping.xml")
public class OxmTest {
	// 스프링 테스트가 테스트용 애플리케이션 컨텍스트에서 Unmarshaller 인터페이스 타입의 빈을 찾아서 테스트가 시작되기 전에 이 변수에 넣어준다.
	@Autowired
	Unmarshaller unmarshaller;

	@Test
	public void unmarshallSqlMap() throws IOException {
		// InputStream을 이용하는 Source 타입의 StreamSource를 만든다.
		Source xmlSource = new StreamSource(getClass().getResourceAsStream("/sqlmap.xml"));

		// 어떤 OXM 기술이든 언마샬은 이 한 줄이면 끝이다.
		Sqlmap sqlmap = (Sqlmap)this.unmarshaller.unmarshal(xmlSource);

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
