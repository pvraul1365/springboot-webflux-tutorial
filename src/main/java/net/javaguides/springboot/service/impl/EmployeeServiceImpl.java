package net.javaguides.springboot.service.impl;

import lombok.RequiredArgsConstructor;
import net.javaguides.springboot.dto.EmployeeDto;
import net.javaguides.springboot.entity.Employee;
import net.javaguides.springboot.mapper.EmployeeMapper;
import net.javaguides.springboot.repository.EmployeeRepository;
import net.javaguides.springboot.service.EmployeeService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * EmployeeServiceImpl
 * <p>
 * Created by IntelliJ, Spring Framework Guru.
 *
 * @author architecture - raul.perez.vicente@gmail.com
 * @version 06/10/2026 - 13:58
 * @since 1.25
 */
@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    
    private final EmployeeRepository employeeRepository;
    
    @Override
    public Mono<EmployeeDto> saveEmployee(final EmployeeDto employeeDto) {
        
        final Employee employee = EmployeeMapper.mapToEntity(employeeDto);

        final Mono<Employee> savedEmployee = employeeRepository.save(employee);

        return savedEmployee.map(EmployeeMapper::mapToDto);
    }

    @Override
    public Mono<EmployeeDto> getEmployeeById(final String employeeId) {

        final Mono<Employee> savedEmployee = employeeRepository.findById(employeeId);

        return savedEmployee.map(EmployeeMapper::mapToDto);
    }

    @Override
    public Flux<EmployeeDto> getAllEmployees() {
        final Flux<Employee> allEmployees = employeeRepository.findAll();

        return allEmployees
                .map(EmployeeMapper::mapToDto)
                .switchIfEmpty(Flux.empty());
    }

    @Override
    public Mono<EmployeeDto> updateEmployee(final EmployeeDto employeeDto, final String employeeId) {
        Mono<Employee> employeeMono = employeeRepository.findById(employeeId);

        Mono<Employee> updatedEmployee = employeeMono.flatMap(existingEmployee -> {
            existingEmployee.setFirstName(employeeDto.getFirstName());
            existingEmployee.setLastName(employeeDto.getLastName());
            existingEmployee.setEmail(employeeDto.getEmail());

            return employeeRepository.save(existingEmployee);
        });

        return updatedEmployee.map(EmployeeMapper::mapToDto);
    }
}
