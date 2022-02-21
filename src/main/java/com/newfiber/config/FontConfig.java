package com.newfiber.config;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.InputStream;
import java.util.Objects;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : X.K
 * @since : 2022/2/21 上午9:45
 */
@Configuration
public class FontConfig {

	@Bean
	public void registerFont() throws Exception{
		InputStream inputStream = Objects.requireNonNull(Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("font/SIMSUN.TTC"));
		Font font = Font.createFont(Font.TRUETYPE_FONT, inputStream);
		GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
	}
}
