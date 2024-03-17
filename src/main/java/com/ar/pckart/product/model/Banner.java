package com.ar.pckart.product.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Banner {

	private String id;
    private String bannerHeader;
    private String description1;
    private String description2;
    private byte[] bannerImage;
	private String imageName;
	private String imageType;
//    private Long productId;
//    private String productName;
	private Product product;
    private boolean enabled=true;
}

/*
 * 0 < length <= 255 --> `TINYBLOB` 255 < length <= 65535 --> `BLOB` 65535 <
 * length <= 16777215 --> `MEDIUMBLOB` 16777215 < length <= 2³¹-1 --> `LONGBLOB`
 */
