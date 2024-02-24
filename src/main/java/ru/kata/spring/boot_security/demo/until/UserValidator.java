package ru.kata.spring.boot_security.demo.until;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UsersService;

@Component
public class UserValidator implements Validator {

    private final UsersService usersService;

    /**
     * Конструктор валидатора.
     *
     * @param usersService Сервис для работы с пользователями.
     */
    public UserValidator(UsersService usersService) {
        this.usersService = usersService;
    }

    /**
     * Поддерживает ли валидатор данную класс.
     *
     * @param clazz Класс для проверки.
     * @return true, если класс поддерживается, false в противном случае.
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    /**
     * Выполняет валидацию объекта Person.
     *
     * @param target Объект для валидации.
     * @param errors Объект для сохранения ошибок валидации.
     */
    @Override
    public void validate(Object target, Errors errors) {
        User person = (User) target;
        if (usersService.loadUserByUsername(person.getName()).isPresent()
                && !usersService.loadUserByUsername(person.getName()).orElse(null).equals(target)) {
            errors.rejectValue("name", "", "A user with that name already exists");
        }
    }
}
