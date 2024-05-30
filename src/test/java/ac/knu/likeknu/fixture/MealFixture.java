package ac.knu.likeknu.fixture;

import ac.knu.likeknu.domain.Cafeteria;
import ac.knu.likeknu.domain.Menu;
import ac.knu.likeknu.domain.constants.Campus;
import ac.knu.likeknu.domain.constants.MealType;

import java.time.LocalDate;

public final class MealFixture {

    public static Cafeteria createCafeteria() {
        return Cafeteria.builder()
                .cafeteriaName("학생식당")
                .campus(Campus.SINGWAN)
                .build();
    }

    public static Menu createMenuWith(Cafeteria cafeteria) {
        return Menu.builder()
                .menus("밥 국 김치")
                .mealType(MealType.LUNCH)
                .menuDate(LocalDate.now())
                .cafeteria(cafeteria)
                .build();
    }
}
