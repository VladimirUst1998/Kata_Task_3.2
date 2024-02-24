package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.UserRepository;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;


@Service
@Transactional
public class UsersService {
    private final UserRepository userRepository;

    /**
     * Конструктор сервиса.
     *
     * @param userRepository Репозиторий для работы с пользователями.
     */
    public UsersService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Загружает пользователя по имени пользователя.
     *
     * @param name Имя пользователя.
     * @return Объект Optional, содержащий информацию о пользователе, если найден.
     * @throws UsernameNotFoundException если пользователь не найден.
     */
    public Optional<User> loadUserByUsername(String name) throws UsernameNotFoundException {
        return userRepository.findByName(name);
    }

    /**
     * Получает роли пользователя по его имени.
     *
     * @param name Имя пользователя.
     * @return Множество ролей пользователя.
     */
    public Set<Role> getUserRoles(String name) {
        Optional<User> userOptional = userRepository.findByName(name);
        return userOptional.map(User::getRoles).orElse(Collections.emptySet());
    }
}
