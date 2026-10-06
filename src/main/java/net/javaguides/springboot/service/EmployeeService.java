package net.javaguides.springboot.service;

import net.javaguides.springboot.dto.EmployeeDto;
import reactor.core.publisher.Mono;

/**
 * EmployeeService
 * <p>
 * Created by IntelliJ, Spring Framework Guru.
 *
 * @author architecture - raul.perez.vicente@gmail.com
 * @version 06/10/2026 - 13:57
 * @since 1.25
 */
public interface EmployeeService {

    Mono<EmployeeDto> saveEmployee(EmployeeDto employeeDto);

}
