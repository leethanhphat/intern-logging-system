package vn.vifo.logging.controller;

import vn.vifo.logging.service.MessageSourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController extends AbstractBaseController {
    private final MessageSourceService messageSourceService;



}
