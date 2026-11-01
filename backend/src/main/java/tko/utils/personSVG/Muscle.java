package tko.utils.personSVG;

import lombok.Getter;

@Getter
public enum Muscle {
    STERNOCLEIDOMASTOIDEUS("sternocleidomastoideus", "Грудино-ключично-сосцевидная мышца"),
    TRAPEZIUS("trapezius", "Трапециевидная мышца"),
    DELTOIDEUS("deltoideus", "Дельтовидная мышца"),
    BICEPS("biceps", "Двуглавая мышца плеча (бицепс)"),
    FOREARMS("forearms", "Предплечье"),
    PECTORALIS_MAJOR("pectoralis_major", "Грудная мышца"),
    OBLIQUUS_EXTERNUS_ABDOMINIS("obliquus_externus_abdominis", "Наружная косая мышца живота"),
    RECTUS_ABDOMINIS("rectus_abdominis", "Прямая мышца живота"),
    TENSOR_FASCIAE_LATAE("tensor_fasciae_latae", "Напрягатель широкой фасции бедра"),
    ADDUCTOR_MAGNUS("adductor_magnus", "Приводящие мышцы бедра"),
    RECTUS_FEMORIS("rectus_femoris", "Прямая мышца бедра"),
    VASTUS_LATERALIS("vastus_lateralis", "Латеральная широкая мышца бедра"),
    VASTUS_MEDIALIS("vastus_medialis", "Медиальная широкая мышца бедра"),
    CRUS("crus", "Голень"),
    TRICEPS_BRACHII("triceps_brachii", "Трёхглавая мышца плеча (трицепс)"),
    INFRASPINATUS("infraspinatus", "Подостная мышца"),
    LATISSIMUS_DORSI("latissimus_dorsi", "Широчайшая мышца спины"),
    GLUTEUS_MEDIUS("gluteus_medius", "Средняя ягодичная мышца"),
    GLUTEUS_MAXIMUS("gluteus_maximus", "Большая ягодичная мышца"),
    BICEPS_FEMORIS("biceps_femoris", "Двуглавая мышца бедра (бицепс бедра)"),
    GASTROCNEMIUS("gastrocnemius", "Икроножная мышца");


    private final String id;
    private final String name;

    Muscle(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public static Muscle getById(String id) {
        for (Muscle muscle : Muscle.values()) {
            if (muscle.getId().equals(id)) {
                return muscle;
            }
        }
        return null;
    }
}
