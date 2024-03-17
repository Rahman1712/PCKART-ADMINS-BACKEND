package com.ar.pckart.product.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ar.pckart.product.model.Banner;
import com.ar.pckart.product.service.BannerService;

@RestController
@RequestMapping("/api/v1/banner")
@PreAuthorize("hasAnyRole({'ADMIN','ADMINTRAINEE','EDITOR'})") //@PreAuthorize("hasRole('ROLE_ADMIN')")
public class BannerServiceController {

	@Autowired private BannerService bannerService;
	
	@PostMapping("/save")
	@PreAuthorize("hasAuthority('store:create')")
    public ResponseEntity<?> saveBanner(
    		@RequestPart("banner") Banner banner,
    		@RequestParam("file") MultipartFile file
    		) {
		String bannerSaved = bannerService.addBanner(banner, file);
		return ResponseEntity.ok(bannerSaved);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('store:update')")
    public ResponseEntity<?> saveBanner(
    		@PathVariable("id") String id,
    		@RequestPart("banner") Banner banner,
	        @RequestParam("file") MultipartFile file) {

		return ResponseEntity.ok(bannerService.updateBanner(id, banner, file));
    }

    @DeleteMapping("/delete/byid/{id}")
    @PreAuthorize("hasAuthority('store:delete')")
    public ResponseEntity<String> delete(@PathVariable String id){
       return ResponseEntity.ok(bannerService.deleteBanner(id));
    }

    @PutMapping("/update/enabled/{id}")
    @PreAuthorize("hasAuthority('store:update')")
    public ResponseEntity<String> updateBannerEnabledById(
    		@PathVariable("id") String id,
    		@RequestParam("enabled")boolean enabled) {
        return ResponseEntity.ok(bannerService.updateEnabledById(id, enabled));
    }
}
