package ac.knu.likeknu.controller.dto.response;

import ac.knu.likeknu.domain.value.Campus;
import ac.knu.likeknu.domain.value.MealType;
import ac.knu.likeknu.domain.value.CafeteriaName;
import ac.knu.likeknu.domain.Menu;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

public class MainMenuResponse {

    private List<String> menu;
    private Campus campus;
    private CafeteriaName cafeteria;
    private MealType mealType;
    private LocalDate date;

    @Builder
    public MainMenuResponse(List<String> menu, Campus campus, CafeteriaName cafeteria, MealType mealType, LocalDate date) {
        this.menu = menu;
        this.campus = campus;
        this.cafeteria = cafeteria;
        this.mealType = mealType;
        this.date = date;
    }

    public static MainMenuResponse of(Menu menu) {
        List<String> menus = List.of(menu.getMenus().split(" "));

        return MainMenuResponse.builder()
                .menu(menus)
                .campus(menu.getCampus())
                .cafeteria(menu.getCafeteria())
                .mealType(menu.getMealType())
                .date(menu.getDate())
                .build();
    }
}
