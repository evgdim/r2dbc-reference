package com.github.evgdim.r2dbc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.function.DatabaseClient;

import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;

@SpringBootApplication
public class R2dbcReferenceApplication {

	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(R2dbcReferenceApplication.class, args);
		
		PostgresqlConnectionFactory connectionFactory = new PostgresqlConnectionFactory(PostgresqlConnectionConfiguration.builder()
				.host("localhost")
				.database("postgres")
				.username("postgres")
				.password("example").build());

		DatabaseClient db = DatabaseClient.create(connectionFactory);
		
		db.execute().sql("insert into role (role_name) values('admin')").fetch().rowsUpdated().subscribe();
		db.execute()
				.sql("SELECT role_name FROM role")
				.exchange()
				.flatMapMany(
						it -> it.extract((r, md) -> r.get(0, String.class)).all()
				)
		.subscribe(System.out::println, System.err::println, () -> System.out.println("Done")); 
		
		Thread.sleep(3000L);
	}
}
