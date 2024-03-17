package com.ar.pckart.product.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import com.ar.pckart.admin.config.JwtProductsService;
import com.ar.pckart.product.model.Banner;
import com.ar.pckart.product.util.ProductsUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BannerService {

	private final WebClient webClient;
	private final JwtProductsService jwtProductsService;
	
	@Value("${banner.service.api.url.add}")
	private String BANNER_SERVICE_URL_ADD;
	
	@Value("${banner.service.api.url.delete}")
	private String BANNER_SERVICE_URL_DELETE;
	
	@Value("${banner.service.api.url.update}")
	private String BANNER_SERVICE_URL_UPDATE;
	
	@Value("${banner.service.api.url.update_enabled}")
	private String BANNER_SERVICE_URL_UPDATE_ENABLED;
	
	public String addBanner(Banner banner, MultipartFile file) {
		MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();
		formData.add("banner", banner);
		System.err.println("eeeeeeeeeeeeeeeeeeeeeeeee");
		formData.add("file", new FileSystemResource(ProductsUtils.convertMultipartFileToFile(file)));
		System.err.println("qqqqqqqqqqqqqqqqqqqqqqqq");
		
		String bannerSaved = webClient.post()
		.uri(BANNER_SERVICE_URL_ADD)
		.header(HttpHeaders.AUTHORIZATION,
				"Bearer "+jwtProductsService.generateToken(ProductsUtils.getAuthenticationUsername()))
		.header("Username", ProductsUtils.getAuthenticationUsername())
		.accept(MediaType.APPLICATION_JSON)
		.contentType(MediaType.MULTIPART_FORM_DATA)
		.body(BodyInserters.fromMultipartData(formData))
		.retrieve()
		.onStatus(HttpStatusCode::isError, response-> ProductsUtils.handleErrorResponse(response))
		.bodyToMono(String.class).block();
		
		System.err.println("PPPPPPPPPPPPPPPPPPPP");
		ProductsUtils.deleteAllFilesInFolderAfterStream();
		System.err.println(bannerSaved);
		
		
		return bannerSaved;
	}


	public String updateBanner(String id, Banner banner, MultipartFile file) {
		MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();
		formData.add("banner", banner);
		formData.add("file", new FileSystemResource(ProductsUtils.convertMultipartFileToFile(file)));
		
		String updateMessage = webClient.put()
		.uri(BANNER_SERVICE_URL_UPDATE+id)
		.header(HttpHeaders.AUTHORIZATION,
				"Bearer "+jwtProductsService.generateToken(ProductsUtils.getAuthenticationUsername()))
		.header("Username", ProductsUtils.getAuthenticationUsername())
		.accept(MediaType.APPLICATION_JSON)
		.contentType(MediaType.MULTIPART_FORM_DATA)
		.body(BodyInserters.fromMultipartData(formData))
		.retrieve()
		.onStatus(HttpStatusCode::isError, response-> ProductsUtils.handleErrorResponse(response))
		.bodyToMono(String.class).block();
		
		ProductsUtils.deleteAllFilesInFolderAfterStream();
		
		return updateMessage;
	}


	public String deleteBanner(String id) {
		return webClient.delete()
				.uri(BANNER_SERVICE_URL_DELETE+id)
				.header(HttpHeaders.AUTHORIZATION,
						"Bearer "+jwtProductsService.generateToken(ProductsUtils.getAuthenticationUsername()))
				.header("Username", ProductsUtils.getAuthenticationUsername())
				.retrieve()
				.onStatus(HttpStatusCode::isError, response-> ProductsUtils.handleErrorResponse(response))
				.bodyToMono(String.class)
				.block();
	}
		
	public String updateEnabledById(String id, boolean enabled) {
		UriComponentsBuilder uriBuilder = UriComponentsBuilder
				.fromUriString(BANNER_SERVICE_URL_UPDATE_ENABLED+id)
				.queryParam("enabled", enabled);
		
		String uriPathWithQueryParams = uriBuilder.toUriString();
		return webClient.put()
				.uri(uriPathWithQueryParams)
				.header(HttpHeaders.AUTHORIZATION,
						"Bearer "+jwtProductsService.generateToken(ProductsUtils.getAuthenticationUsername()))
				.header("Username", ProductsUtils.getAuthenticationUsername())
				.retrieve()
				.onStatus(HttpStatusCode::isError, response-> ProductsUtils.handleErrorResponse(response))
				.bodyToMono(String.class)
				.block();
	}	
}
