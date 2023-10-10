package apiserver.apiserver.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig{

	private final JwtAuthConverter jwtAuthConverter;

	@Autowired
	public SecurityConfig(final JwtAuthConverter jwtAuthConverter) {
		this.jwtAuthConverter = jwtAuthConverter;
	}

	@Bean
	public SecurityFilterChain configure(final HttpSecurity http) throws Exception {
		((AuthorizeHttpRequestsConfigurer.AuthorizedUrl) ((AuthorizeHttpRequestsConfigurer.AuthorizedUrl) ((HttpSecurity) 
				http
				.csrf().disable())
				.authorizeHttpRequests().requestMatchers(new String[] { "*" })).authenticated()
				.anyRequest()).permitAll();
		http.oauth2ResourceServer().jwt().jwtAuthenticationConverter((Converter)this.jwtAuthConverter);
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		return (SecurityFilterChain) http.build();
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedMethods("*");
			}
		};
	}
}