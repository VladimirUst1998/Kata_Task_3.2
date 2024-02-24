package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.security.PersonDetails;
import ru.kata.spring.boot_security.demo.service.AdminService;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.until.RoleValidator;
import ru.kata.spring.boot_security.demo.until.UserValidator;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;
    private final RoleService roleService;
    private final UserValidator userValidator;
    private final RoleValidator roleValidator;

    /**
     * Конструктор контроллера.
     *
     * @param adminService    Сервис для работы с пользователями.
     * @param roleService     Сервис для работы с ролями.
     * @param userValidator   Валидатор для объектов User.
     * @param roleValidator   Валидатор для объектов Role.
     */
    @Autowired
    public AdminController(AdminService adminService, RoleService roleService, UserValidator userValidator, RoleValidator roleValidator) {
        this.adminService = adminService;
        this.roleService = roleService;
        this.userValidator = userValidator;
        this.roleValidator = roleValidator;
    }

    /**
     * Отображает страницу с информацией о текущем пользователе.
     *
     * @param model Модель для передачи данных в представление.
     * @return Строка с именем представления "user/userPage".
     */
    @GetMapping("/user")
    public String showAdminInfo(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        model.addAttribute("personDetails", personDetails);
        return "admin/adminPage";
    }

    /**
     * Получает страницу со списком всех пользователей.
     *
     * @param model     Модель для передачи данных в представление.
     * @param principal Объект Principal для получения информации об аутентифицированном пользователе.
     * @return Строка с именем представления "admin/users".
     */
    @GetMapping("/users")
    public String getAllUsers(Model model, Principal principal) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        model.addAttribute("personDetails", personDetails);
        User user = adminService.findUserByUserName(principal.getName());
        model.addAttribute("user", user);
        List<User> userList = adminService.getAllUsers();
        model.addAttribute("userList", userList);
        return "admin/users";
    }

    /**
     * Удаляет пользователя по ID и перенаправляет на страницу со списком пользователей.
     *
     * @param id ID пользователя для удаления.
     * @return Строка с адресом перенаправления "/admin/users".
     */
    @GetMapping("admin/removeUser")
    public String removeUser(@RequestParam("id") Long id) {
        adminService.removeUser(id);
        return "redirect:/admin/users";
    }

    /**
     * Получает форму добавления пользователя.
     * @return Строка с именем представления "admin/userAdd".
     */

    @GetMapping("/new")
    public String getAddUserForm(@ModelAttribute("user") User user) {return "admin/userAdd";}

    /**
     * Обрабатывает форму добавления пользователя и перенаправляет на страницу со списком пользователей.
     *
     * @param user        Объект Person с данными пользователя.
     * @param roles         Список ролей пользователя.
     * @param userBindingResult Результат валидации данных пользователя.
     * @param rolesBindingResult Результат валидации данных роли.
     * @return Строка с адресом перенаправления "/admin/users".
     */

    @PostMapping("/addUser")
    public String postAddUserForm(@ModelAttribute("user") @Valid User user,
                                   BindingResult userBindingResult,
                                   @RequestParam(value = "roles", required = false) @Valid List<String> roles,
                                   BindingResult rolesBindingResult,
                                   RedirectAttributes redirectAttributes) {

        userValidator.validate(user, userBindingResult);
        if (userBindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorsUser", userBindingResult.getAllErrors());
            return "/admin/userAdd";
        }
        roleValidator.validate(roles, rolesBindingResult);
        if (rolesBindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorsRoles", rolesBindingResult.getAllErrors());
            return "/admin/userAdd";
        }
        adminService.addUser(user, roles);
        return "redirect:/admin/users";
    }


    /**
     * Получает форму редактирования пользователя по ID.
     *
     * @param model Модель для передачи данных в представление.
     * @param id    ID пользователя для редактирования.
     * @return Строка с именем представления "admin/userUpdate".
     */
    @GetMapping("/admin/updateUser")
    public String getEditUserForm(Model model, @RequestParam("id") Long id) {
        model.addAttribute("user", adminService.findOneById(id));
        model.addAttribute("roles", roleService.getRoles());
        return "admin/userUpdate";
    }



    /**
     * Обрабатывает форму редактирования пользователя и перенаправляет на страницу со списком пользователей.
     *
     * @param user        Объект Person с данными пользователя.
     * @param roles         Список ролей пользователя.
     * @param rolesBindingResult Результат валидации данных роли.
     * @return Строка с адресом перенаправления "/admin/users".
     */
    @PostMapping("/updateUser")
    public String postEditUserForm(@ModelAttribute("user") @Valid User user,
                                   @RequestParam(value = "roles", required = false) @Valid List<String> roles,
                                   BindingResult rolesBindingResult,
                                   RedirectAttributes redirectAttributes) {

        roleValidator.validate(roles, rolesBindingResult);
        if (rolesBindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorsRoles", rolesBindingResult.getAllErrors());
            return "/admin/userUpdate";
        }

        adminService.updateUser(user, roles);
        return "redirect:/admin/users";
    }

}
