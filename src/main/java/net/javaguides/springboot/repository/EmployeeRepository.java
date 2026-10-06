package net.javaguides.springboot.repository;

import net.javaguides.springboot.entity.Employee;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

/**
 * EmployeeRepository
 * <p>
 * Created by IntelliJ, Spring Framework Guru.
 *
 * @author architecture - raul.perez.vicente@gmail.com
 * @version 06/10/2026 - 13:42
 * @since 1.25
 */
public interface EmployeeRepository extends ReactiveCrudRepository<Employee,String> {
}
