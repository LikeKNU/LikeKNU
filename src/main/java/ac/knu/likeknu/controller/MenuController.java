package ac.knu.likeknu.controller;

import ac.knu.likeknu.controller.dto.base.ResponseDto;
import ac.knu.likeknu.controller.dto.menu.MenuResponse;
import ac.knu.likeknu.domain.value.Campus;
import ac.knu.likeknu.exception.BusinessException;
import ac.knu.likeknu.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/menu")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @GetMapping
    public ResponseDto<List<MenuResponse>> getMenuByCampus(@RequestParam(name = "campus") Campus campus) {
        if (campus.equals(Campus.ALL)) {
            throw new BusinessException("Invalid campus");
        }

        List<MenuResponse> menuResponsesByCampus = menuService.getMenuResponsesByCampus(campus);
        return ResponseDto.of(menuResponsesByCampus);
    }
}
