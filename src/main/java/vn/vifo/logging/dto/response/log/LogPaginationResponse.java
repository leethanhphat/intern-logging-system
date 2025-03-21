package vn.vifo.logging.dto.response.log;

import org.springframework.data.domain.Page;
import vn.vifo.logging.dto.response.PaginationResponse;

import java.util.List;

public class LogPaginationResponse extends PaginationResponse<LogResponse> {
    public LogPaginationResponse(final Page<?> pageModel, final List<LogResponse> items) {
        super(pageModel, items);
    }
}
