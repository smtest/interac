package com.ibm.interac.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableResourceServer
public class ResourceServerSecurityConfig extends ResourceServerConfigurerAdapter
{
	@Value("${bigoauth2server.oauth2.signingKey}")
	private String tokenSigningKey;

	// @Autowired
	// UserDetailsService userDetailsService;

	// @Autowired
	// ClientDetailsService cds;

	@Bean
	public JwtAccessTokenConverter accessTokenConverter()
	{
		JwtAccessTokenConverter jatc = new JwtAccessTokenConverter();

		DefaultAccessTokenConverter defaultAccessTokenConverter = new DefaultAccessTokenConverter();

		jatc.setAccessTokenConverter(defaultAccessTokenConverter);

		jatc.setSigningKey(tokenSigningKey);

		return jatc;
	}

	@Bean
	public TokenStore tokenStore()
	{
		return new JwtTokenStore(accessTokenConverter());
	}

	@Bean
	public WebMvcConfigurer corsConfigurer()
	{
		return new WebMvcConfigurerAdapter()
		{
			@Override
			public void addCorsMappings(CorsRegistry registry)
			{
				System.out.println("adding cors support");
				registry.addMapping("/**").allowedOrigins("*").allowedHeaders("*").allowedMethods("OPTIONS", "HEAD",
						"GET", "PUT", "POST", "PATCH", "DELETE");
			}
		};
	}

	@Bean
	@Primary
	public ResourceServerTokenServices resourceServerTokenServices()
	{
		DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
		defaultTokenServices.setTokenStore(tokenStore());
		defaultTokenServices.setTokenEnhancer(accessTokenConverter());
		defaultTokenServices.setSupportRefreshToken(true);

		/**
		 * Add this line when the client has to be validated on the server side.
		 */
		// defaultTokenServices.setClientDetailsService(cds);

		return defaultTokenServices;
	}

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception
	{
		resources.tokenServices(resourceServerTokenServices());
	}

	@Override
	public void configure(HttpSecurity http) throws Exception
	{
		http.authorizeRequests().antMatchers(HttpMethod.OPTIONS, "/**").permitAll();
		http.authorizeRequests()
				.antMatchers("/webjars/**", "/swagger-ui.html", "/swagger-resources", "/v2/api-docs", "/admin/**",
						"/metrics", "/hystrix", "/hystrix.stream", "/health", "/routes", "/hystrix/**", "/info",
						"/env/**", "/metrics/**", "/mappings", "/proxy**/**")
				.permitAll();
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().authorizeRequests()
				.anyRequest().authenticated();
	}
}
