package ac.knu.likeknu.service;

import ac.knu.likeknu.controller.dto.menu.MealListDto;
import ac.knu.likeknu.controller.dto.menu.MenuResponse;
import ac.knu.likeknu.domain.Cafeteria;
import ac.knu.likeknu.domain.value.Campus;
import ac.knu.likeknu.domain.value.MealType;
import ac.knu.likeknu.repository.CafeteriaRepository;
import ac.knu.likeknu.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    private final CafeteriaRepository cafeteriaRepository;

    public List<MenuResponse> getMenuResponsesByCampus(Campus campus) {
        List<Cafeteria> cafeterias = cafeteriaRepository.findByCampus(campus);

        return cafeterias.stream()
                .sorted(Comparator.comparing(cafeteria -> cafeteria.getCafeteriaName().getSequence()))
                .map(cafeteria -> MenuResponse.of(cafeteria, filterMealtypeAndCreateList(cafeteria)))
                .collect(Collectors.toList());
    }

    /**
     * MealType의 요소들을 이용해 밤이나 새벽이 아닐 경우 MealListDto 리스트를 만듭니다.
     *
     * @param cafeteria
     *
     * @return
     */
    private List<MealListDto> filterMealtypeAndCreateList(Cafeteria cafeteria) {
        return Arrays.stream(MealType.values())
                .filter(mealType -> !isMealTypeDawnOrNight(mealType))
                .map(mealType -> findRepositoryAndMapDto(mealType, cafeteria))
                .collect(Collectors.toList());
    }

    /**
     * 레포지토리에서 현재 시간과 cafeteria, mealType을 이용해 데이터를 가져온 후 MealListDto로 매핑합니다.
     *
     * @param mealType
     * @param cafeteria
     *
     * @return
     */
    private MealListDto findRepositoryAndMapDto(MealType mealType, Cafeteria cafeteria) {
        return menuRepository.findByMenuDateAndCafeteriaAndMealType(LocalDate.now(), cafeteria, mealType)
                .map(menu -> MealListDto.of(mealType, cafeteria, menu.getMenus()))
                .orElse(MealListDto.empty(mealType));
    }

    private boolean isMealTypeDawnOrNight(MealType mealType) {
        return mealType.equals(MealType.DAWN) || mealType.equals(MealType.NIGHT);
    }
}
