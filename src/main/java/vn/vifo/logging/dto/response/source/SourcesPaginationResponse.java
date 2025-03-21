package vn.vifo.logging.dto.response.source;

import org.springframework.data.domain.Page;
import vn.vifo.logging.dto.response.PaginationResponse;

import java.util.List;

public class SourcesPaginationResponse extends PaginationResponse<SourceResponse> {
    public SourcesPaginationResponse(Page<?> pageModel, List<SourceResponse> items) {
        super(pageModel, items);
    }
}
