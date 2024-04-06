package com.gaga.springtoby;

import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.*;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import com.gaga.springtoby.user.sqlService.EmbeddedDbSqlRegistry;
import com.gaga.springtoby.user.sqlService.OxmSqlService;
import com.gaga.springtoby.user.sqlService.SqlRegistry;
import com.gaga.springtoby.user.sqlService.SqlService;

/**
 * SQL 서비스 빈 설정을 위한 SqlServiceContext 클래스
 */
@Configuration
public class SqlServiceContext {
	@Autowired
	SqlMapConfig sqlMapConfig;

	/* sqlService 빈 정의
<bean id="sqlService" class="com.gaga.springtoby.user.sqlService.OxmSqlService">
	<property name="unmarshaller" ref="unmarshaller"/>
	<property name="sqlRegistry" ref="sqlRegistry"/>
 */
	@Bean
	public SqlService sqlService() {
		OxmSqlService sqlService = new OxmSqlService();
		sqlService.setUnmarshaller(unmarshaller());
		sqlService.setSqlRegistry(sqlRegistry());
		// sqlService.setSqlmap(new ClassPathResource("/usersqlmap.xml", UserDao.class));
		sqlService.setSqlmap(this.sqlMapConfig.getSqlMapResource());
		return sqlService;
	}

	/* sqlRegistry 빈 정의
	<bean id="sqlRegistry" class="com.gaga.springtoby.user.sqlService.EmbeddedDbSqlRegistry">
        <property name="dataSource" ref="embeddedDatabase"/>
    </bean>
	 */
	// @Resource DataSource embeddedDatabase;
	@Bean
	public SqlRegistry sqlRegistry() {
		EmbeddedDbSqlRegistry sqlRegistry = new EmbeddedDbSqlRegistry();
		sqlRegistry.setDataSource(embeddedDatabase());
		return sqlRegistry;
	}

	/* unmarshaller 빈 정의
	<bean id="unmarshaller" class="org.springframework.oxm.jaxb.Jaxb2Marshaller">
        <property name="contextPath" value="com.gaga.springtoby.user.sqlService.jaxb"/>
    </bean>
	 */
	@Bean
	public Unmarshaller unmarshaller() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setContextPath("com.gaga.springtoby.user.sqlService.jaxb");
		return marshaller;
	}

	/* embeddedDatabase 빈 정의
	<jdbc:embedded-database id="embeddedDatabase" type="HSQL">
        <jdbc:script location="classpath:sqlRegistrySchema.sql"/>
    </jdbc:embedded-database>
	 */
	@Bean
	public DataSource embeddedDatabase() {
		return new EmbeddedDatabaseBuilder()
			.setName("embeddedDatabase")
			.setType(HSQL)
			.addScript("classpath:sqlRegistrySchema.sql")
			.build();
	}
}
