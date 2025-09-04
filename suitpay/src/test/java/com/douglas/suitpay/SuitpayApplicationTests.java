package com.douglas.suitpay;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
class SuitpayApplicationTests {
    // pode - se implementar testes unitarios para cobrir as funcionalidades do desafio suitpay
	@Test
	void contextLoads() {
		assertThat(true).isTrue();
	}

	@Test
	void simpleMathTest() {
		int sum = 2 + 3;
		assertThat(sum).isEqualTo(5);
	}
}