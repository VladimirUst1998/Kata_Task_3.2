package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RegistrationService;
import ru.kata.spring.boot_security.demo.until.UserValidator;

import javax.validation.Valid;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final RegistrationService registrationService;
    private final UserValidator userValidator;

    /**
     * Конструктор контроллера.
     *
     * @param registrationService Сервис для регистрации пользователей.
     * @param userValidator    Валидатор для объектов User.
     */
    @Autowired
    public AuthController(RegistrationService registrationService, UserValidator userValidator) {
        this.registrationService = registrationService;
        this.userValidator = userValidator;
    }

    /**
     * Отображает страницу входа.
     *
     * @return Строка с именем представления "security/login".
     */
    @GetMapping("login")
    public String loginPage() {
        return "security/login";
    }

    /**
     * Отображает страницу регистрации.
     *
     * @param user Объект User для передачи данных на страницу.
     * @return Строка с именем представления "security/registration".
     */
    @GetMapping("registration")
    public String registrationPage(@ModelAttribute("user") User user) {
        return "security/registration";
    }

    /**
     * Обрабатывает форму регистрации и перенаправляет на страницу входа.
     *
     * @param user         Объект User с данными пользователя.
     * @param bindingResult  Результат валидации данных пользователя.
     * @return Строка с адресом перенаправления "/auth/login".
     */
    @PostMapping("/registration")
    public String performRegistration(@ModelAttribute("user") @Valid User user,
                                      BindingResult bindingResult) {

        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            return "security/registration";
        }
        registrationService.registerUser(user);

        return "redirect:/auth/login";
    }
}
