package net.javaguides.springboot.mapper;

import net.javaguides.springboot.dto.EmployeeDto;
import net.javaguides.springboot.entity.Employee;

/**
 * EmployeeMapper
 * <p>
 * Created by IntelliJ, Spring Framework Guru.
 *
 * @author architecture - raul.perez.vicente@gmail.com
 * @version 06/10/2026 - 13:51
 * @since 1.25
 */
public class EmployeeMapper {

    public static EmployeeDto mapToDto(final Employee employee) {
        return EmployeeDto.builder()
                .id(employee.getId())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .email(employee.getEmail())
                .build();
    }

    public static Employee mapToEntity(final EmployeeDto employeeDto) {
        return Employee.builder()
                .id(employeeDto.getId())
                .firstName(employeeDto.getFirstName())
                .lastName(employeeDto.getLastName())
                .email(employeeDto.getEmail())
                .build();
    }

}
