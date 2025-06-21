package sigma.examino.model;

/**
 * Enum reprezentujący role użytkowników w systemie.
 * Określa uprawnienia dostępu dla konta użytkownika.
 */
public enum Role {
    /**
     * Rola zwykłego użytkownika – studenta rozwiązującego egzaminy.
     */
    STUDENT,

    /**
     * Rola administratora – uprawnienia do zarządzania egzaminami i użytkownikami.
     */
    ADMIN
}
