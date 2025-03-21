package vn.vifo.logging.dto.response.logcategory;

import org.springframework.data.domain.Page;
import vn.vifo.logging.dto.response.PaginationResponse;

import java.util.List;

public class LogCategoryPaginationResponse extends PaginationResponse<LogCategoryResponse> {
    public LogCategoryPaginationResponse(final Page<?> pageModel, final List<LogCategoryResponse> items) {
        super(pageModel, items);
    }
}