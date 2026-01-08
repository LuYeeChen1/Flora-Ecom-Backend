package com.backend.flowershop.application.ruleimpl.seller;

import com.backend.flowershop.application.pipeline.RuleContext;
import com.backend.flowershop.application.rules.seller.SellerDocumentRule;
import com.backend.flowershop.application.validator.ValidationError;
import com.backend.flowershop.domain.model.seller.SellerDocument;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 作用：文件可选；若提供则做最小有效性校验（SL040）
 */
@Component
@Order(40)
public class SL040DocumentsOptionalButIfPresentValid implements SellerDocumentRule {

    @Override
    public void validate(RuleContext context, List<ValidationError> errors) {
        List<SellerDocument> docs = context.sellerDocuments();
        if (docs == null || docs.isEmpty()) return;

        for (int i = 0; i < docs.size(); i++) {
            SellerDocument d = docs.get(i);
            if (d == null) continue;

            String prefix = "documents[" + i + "].";
            if (isBlank(d.type())) errors.add(new ValidationError("SELLER_DOC_TYPE_REQUIRED", prefix + "type", "文件类型必填"));
            if (isBlank(d.number())) errors.add(new ValidationError("SELLER_DOC_NUMBER_REQUIRED", prefix + "number", "文件编号必填"));

            // url 最小校验：如果有则必须 http/https
            if (!isBlank(d.url()) && !(d.url().startsWith("http://") || d.url().startsWith("https://"))) {
                errors.add(new ValidationError("SELLER_DOC_URL_INVALID", prefix + "url", "url 必须以 http/https 开头"));
            }
        }
    }

    private boolean isBlank(String s) {
        return s == null || s.isBlank();
    }
}
