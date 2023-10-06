package ac.knu.likeknu.controller.dto.main;

import ac.knu.likeknu.controller.dto.menu.MenuListDto;
import ac.knu.likeknu.domain.Cafeteria;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class MainMenuResponse {

    private final String cafeteriaId;
    private final String cafeteriaName;
    private final List<MenuListDto> menus;

    @Builder
    public MainMenuResponse(String cafeteriaId, String cafeteriaName, List<MenuListDto> menus) {
        this.cafeteriaId = cafeteriaId;
        this.cafeteriaName = cafeteriaName;
        this.menus = menus;
    }

    public static MainMenuResponse of(Cafeteria cafeteria, String menu) {
        List<MenuListDto> menuList = new ArrayList<>();
        if (menu != null) {
            String[] menus = menu.split(" ");
            for (int i = 0; i < menus.length; i++) {
                menuList.add(MenuListDto.of(i + 1, menus[i]));
            }
        }

        return MainMenuResponse.builder()
                .cafeteriaId(cafeteria.getId())
                .cafeteriaName(cafeteria.getCafeteriaName().getCafeteriaName())
                .menus(menuList)
                .build();
    }

    public static MainMenuResponse empty(Cafeteria cafeteria) {
        return MainMenuResponse.builder()
                .cafeteriaId(cafeteria.getId())
                .cafeteriaName(cafeteria.getCafeteriaName().getCafeteriaName())
                .build();
    }
}
