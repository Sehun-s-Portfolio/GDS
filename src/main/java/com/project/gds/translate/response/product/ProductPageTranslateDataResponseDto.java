package com.project.gds.translate.response.product;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ProductPageTranslateDataResponseDto {
    private String productHeadText;
    private List<String> productHead;
    private String productNameText;
    private String productDescriptText;
    private List<String> productDescript;
}
