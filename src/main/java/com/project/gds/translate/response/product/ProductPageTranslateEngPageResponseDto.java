package com.project.gds.translate.response.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@NoArgsConstructor
@AllArgsConstructor
@PropertySource(value = "classpath:/application-translate.properties")
@Builder
@Getter
@Component
public class ProductPageTranslateEngPageResponseDto {
    @Value("${gds.product.eng.productHeadText}")
    private String productHeadText;

    @Value("${gds.product.eng.productNameText}")
    private String productNameText;

    @Value("${gds.product.eng.productDescriptText}")
    private String productDescriptText;

}
