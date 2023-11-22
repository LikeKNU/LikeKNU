package ac.knu.likeknu.domain.value;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.stream.Stream;

@Getter
public enum MealType {

    BREAKFAST("아침", 9),
    LUNCH("점심", 14),
    DINNER("저녁", 19),
    NIGHT("밤", 21),
    DAWN("새벽", 2);

    private final String mealTypeKr;
    private final int hour;

    MealType(String mealTypeKr, int hour) {
        this.mealTypeKr = mealTypeKr;
        this.hour = hour;
    }

    public static MealType now() {
        int hour = LocalDateTime.now().getHour();

        return Stream.of(MealType.values())
                .filter((MealType m) -> m.getHour() > hour)
                .findFirst()
                .orElse(NIGHT);
    }
}
