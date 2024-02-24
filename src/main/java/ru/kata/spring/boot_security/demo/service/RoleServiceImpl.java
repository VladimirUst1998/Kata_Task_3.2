package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.RoleRepository;
import ru.kata.spring.boot_security.demo.model.Role;


import java.util.HashSet;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    /**
     * Конструктор сервиса.
     *
     * @param roleRepository Репозиторий для работы с ролями.
     */
    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    /**
     * Получает все роли.
     *
     * @return Множество ролей.
     */
    @Override
    public Set<Role> getRoles() {
        return new HashSet<>(roleRepository.findAll());
    }

    /**
     * Находит роль по ее идентификатору.
     *
     * @param id Идентификатор роли.
     * @return Объект Role, представляющий роль.
     */
    @Override
    public Role findById(Long id) {
        return roleRepository.findById(id).orElse(null);
    }

    /**
     * Находит роль по ее строковому идентификатору.
     *
     * @param id Строковый идентификатор роли.
     * @return Объект Role, представляющий роль.
     */
    @Override
    public Role findByStringId(String id) {
        Long LongId = Long.parseLong(id);
        return roleRepository.findById(LongId).orElse(null);
    }

    /**
     * Находит роль по ее имени.
     *
     * @param name Имя роли.
     * @return Объект Role, представляющий роль.
     */
    @Override
    public Role findByName(String name) {
        return roleRepository.findByNameOfRole(name);
    }

    /**
     * Сохраняет роль.
     *
     * @param role Объект Role для сохранения.
     */
    @Override
    @Transactional
    public void saveRole(Role role) {
        roleRepository.save(role);
    }
}
