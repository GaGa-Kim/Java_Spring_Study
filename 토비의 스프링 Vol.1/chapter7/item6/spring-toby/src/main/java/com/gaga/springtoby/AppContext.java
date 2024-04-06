package com.gaga.springtoby;

import java.sql.Driver;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.gaga.springtoby.user.dao.UserDao;
import com.gaga.springtoby.user.service.DummyMailSender;
import com.gaga.springtoby.user.service.TestUserService;
import com.gaga.springtoby.user.service.UserService;

/**
 * DI 메타정보로 사용될 AppContext 클래스
 */
@Configuration
/* 트랜잭션 AOP 기능 사용을 위한 설정
<tx:annotation-driven/>
 */
@EnableTransactionManagement
// @ImportResource("/applicationContext.xml")
@ComponentScan(basePackages = "com.gaga.springtoby.user")
// @Import({SqlServiceContext.class, TestAppContext.class, ProductionAppContext.class})
// @Import(SqlServiceContext.class)
@EnableSqlService
@PropertySource("/database.properties")
public class AppContext implements SqlMapConfig {
	/* @Value로 변경
	@Autowired
	Environment env;
	*/

	@Value("${db.driverClass}")
	Class<? extends Driver> driverClass;
	@Value("${db.url}")
	String url;
	@Value("${db.username}")
	String username;
	@Value("${db.password}")
	String password;

	/* dataSource 빈 정의
	<bean id="dataSource" class="org.springframework.jdbc.datasource.SimpleDriverDataSource">
        <property name="driverClass" value="com.mysql.jdbc.Driver"/>
        <property name="url" value="jdbc:mysql://localhost/toby"/>
        <property name="username" value="toby"/>
        <property name="password" value="gaga"/>
    </bean>
	 */
	@Bean
	public DataSource dataSource() {
		SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
		/* 프로퍼티 소스로 변경
		try {
			Class driverClass = Class.forName("com.mysql.jdbc.Driver");
			dataSource.setDriverClass(driverClass);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		dataSource.setUrl("jdbc:mysql://localhost/toby?characterEncoding=UTF-8");
		dataSource.setUsername("toby");
		dataSource.setPassword("gaga");
		 */

		/* @Value로 변경
		try {
			dataSource.setDriverClass(
				(Class<? extends java.sql.Driver>)Class.forName(env.getProperty("db.driverClass")));
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
		dataSource.setUrl(env.getProperty("db.url"));
		dataSource.setUsername(env.getProperty("db.username"));
		dataSource.setPassword(env.getProperty("db.password"));
		 */
		dataSource.setDriverClass(this.driverClass);
		dataSource.setUrl(this.url);
		dataSource.setUsername(this.username);
		dataSource.setPassword(this.password);
		return dataSource;
	}

	@Bean
	public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	/* transactionManager 빈 정의
	<bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <property name="dataSource" ref="dataSource"/>
    </bean>
	 */
	@Bean
	public PlatformTransactionManager transactionManager() {
		DataSourceTransactionManager tm = new DataSourceTransactionManager();
		tm.setDataSource(dataSource());
		return tm;
	}

	/* @Component(@Repository)를 이용한 자동 빈 등록
	// userDao 빈 정의
	// <bean id="userDao" class="com.gaga.springtoby.user.dao.UserDaoJdbc">
    //    <property name="dataSource" ref="dataSource"/>
    //    <property name="sqlService" ref="sqlService"/>
    // </bean>
    // @Autowired SqlService sqlService;
	@Bean
	public UserDao userDao() {
		/* @Autowired를 이용한 자동와이어링
		   UserDaoJdbc dao = new UserDaoJdbc();
		   dao.setDataSource(dataSource());
		   dao.setSqlService(sqlService()); /
		return new UserDaoJdbc();
	}

	// userService 빈 정의
	// <bean id="userService" class="com.gaga.springtoby.user.service.UserServiceImpl">
    //    <property name="userDao" ref="userDao"/>
    //    <property name="mailSender" ref="mailSender"/>
    // </bean>
	// @Autowired UserDao userDao;
	@Bean
	public UserService userService() {
		UserServiceImpl service = new UserServiceImpl();
		// service.setUserDao(userDao());
		service.setUserDao(this.userDao);
		service.setMailSender(mailSender());
		return service;
	}
	 */

	/* TestAppContext로 이동
	// testUserService 빈 정의
	// <bean id="testUserService"
    //      class="com.gaga.springtoby.user.service.TestUserService"
    //      parent="userService"/>
	@Bean
	public UserService testUserService() {
		TestUserService testUserService = new TestUserService();
		// testUserService.setUserDao(userDao());
		testUserService.setUserDao(this.userDao);
		testUserService.setMailSender(mailSender());
		return testUserService;
	}

	// mailSender 빈 정의
	// <bean id="mailSender" class="com.gaga.springtoby.user.service.DummyMailSender"/>
	@Bean
	public MailSender mailSender() {
		return new DummyMailSender();
	}
	*/

	/* SqlServiceContext로 이동
	// sqlService 빈 정의
	// <bean id="sqlService" class="com.gaga.springtoby.user.sqlService.OxmSqlService">
    //    <property name="unmarshaller" ref="unmarshaller"/>
    //    <property name="sqlRegistry" ref="sqlRegistry"/>
	//
	@Bean
	public SqlService sqlService() {
		OxmSqlService sqlService = new OxmSqlService();
		sqlService.setUnmarshaller(unmarshaller());
		sqlService.setSqlRegistry(sqlRegistry());
		return sqlService;
	}

	// sqlRegistry 빈 정의
	// <bean id="sqlRegistry" class="com.gaga.springtoby.user.sqlService.EmbeddedDbSqlRegistry">
    //    <property name="dataSource" ref="embeddedDatabase"/>
    // </bean>
	//
	// @Resource DataSource embeddedDatabase;
	@Bean
	public SqlRegistry sqlRegistry() {
		EmbeddedDbSqlRegistry sqlRegistry = new EmbeddedDbSqlRegistry();
		sqlRegistry.setDataSource(embeddedDatabase());
		return sqlRegistry;
	}

	// unmarshaller 빈 정의
	// <bean id="unmarshaller" class="org.springframework.oxm.jaxb.Jaxb2Marshaller">
    //    <property name="contextPath" value="com.gaga.springtoby.user.sqlService.jaxb"/>
    // </bean>
	//
	@Bean
	public Unmarshaller unmarshaller() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setContextPath("com.gaga.springtoby.user.sqlService.jaxb");
		return marshaller;
	}

	// embeddedDatabase 빈 정의
	// <jdbc:embedded-database id="embeddedDatabase" type="HSQL">
    //   <jdbc:script location="classpath:sqlRegistrySchema.sql"/>
    // </jdbc:embedded-database>
	//
	@Bean
	public DataSource embeddedDatabase() {
		return new EmbeddedDatabaseBuilder()
			.setName("embeddedDatabase")
			.setType(HSQL)
			.addScript("classpath:sqlRegistrySchema.sql")
			.build();
	}
	 */

	/* ProductionAppContext로 이동
	@Bean
	public MailSender mailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost("mail.mycompany.com");
		return mailSender;
	}
	 */

	@Configuration
	@Profile("production")
	public static class ProductionAppContext {
		@Bean
		public MailSender mailSender() {
			JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
			mailSender.setHost("mail.mycompany.com");
			return mailSender;
		}
	}

	@Configuration
	@Profile("test")
	public static class TestAppContext {
		@Bean
		public UserService testUserService() {
			return new TestUserService();
		}

		@Bean
		public MailSender mailSender() {
			return new DummyMailSender();
		}
	}

	/* AppContext가 SqlMapConfig를 직접 구현하여 제거됨
	@Bean
	public SqlMapConfig sqlMapConfig() {
		return new UserSqlMapConfig();
	}
	 */

	// SqlMapConfig를 직접 구현
	@Override
	public Resource getSqlMapResource() {
		return new ClassPathResource("/usersqlmap.xml", UserDao.class);
	}
}
