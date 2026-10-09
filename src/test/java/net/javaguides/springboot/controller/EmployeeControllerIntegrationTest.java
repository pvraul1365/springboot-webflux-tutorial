package net.javaguides.springboot.controller;

import net.javaguides.springboot.dto.EmployeeDto;
import net.javaguides.springboot.entity.Employee;
import net.javaguides.springboot.repository.EmployeeRepository;
import net.javaguides.springboot.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class EmployeeControllerIntegrationTest {

    @Autowired
    EmployeeService employeeService;

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    EmployeeRepository employeeRepository;

    @BeforeEach
    void setUp() {
        System.out.println("Deleting all employees before each test");
        employeeRepository.deleteAll().subscribe();
    }

    @Test
    void testSaveEmployee() {

        var employeeDto = EmployeeDto.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .build();

        webTestClient.post()
                .uri("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(employeeDto), EmployeeDto.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.firstName").isEqualTo(employeeDto.getFirstName())
                .jsonPath("$.lastName").isEqualTo(employeeDto.getLastName())
                .jsonPath("$.email").isEqualTo(employeeDto.getEmail());
        ;

    }

    @Test
    void testGetSingleEmployee() {

        var employeeDto = EmployeeDto.builder()
                .firstName("Meena")
                .lastName("Fadatare")
                .email("meena.fadatare@example.com")
                .build();

        var savedEmployee = employeeService.saveEmployee(employeeDto).block();

        webTestClient.get()
                .uri("/api/employees/{id}", savedEmployee.getId())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.id").isEqualTo(savedEmployee.getId())
                .jsonPath("$.firstName").isEqualTo(savedEmployee.getFirstName())
                .jsonPath("$.lastName").isEqualTo(savedEmployee.getLastName())
                .jsonPath("$.email").isEqualTo(savedEmployee.getEmail());
    }

    @Test
    void testGetAllEmployees() {

        var employeeDto = EmployeeDto.builder()
                .firstName("Meena")
                .lastName("Fadatare")
                .email("meena.fadatare@example.com")
                .build();

        employeeService.saveEmployee(employeeDto).block();

        employeeDto = EmployeeDto.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .build();

        employeeService.saveEmployee(employeeDto).block();

        webTestClient.get()
                .uri("/api/employees")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(EmployeeDto.class)
                .consumeWith(System.out::println);

    }

    @Test
    void testUpdateEmployee() {

        var employeeDto = EmployeeDto.builder()
                .firstName("Meena")
                .lastName("Fadatare")
                .email("meena.fadatare@example.com")
                .build();

        var savedEmployee = employeeService.saveEmployee(employeeDto).block();

        var updatedEmployeeDto = EmployeeDto.builder()
                .firstName("Meena Updated")
                .lastName("Fadatare Updated")
                .email("meena.updated@example.com")
                .build();

        webTestClient.put()
                .uri("/api/employees/{id}", savedEmployee.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(updatedEmployeeDto), EmployeeDto.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.firstName").isEqualTo(updatedEmployeeDto.getFirstName())
                .jsonPath("$.lastName").isEqualTo(updatedEmployeeDto.getLastName())
                .jsonPath("$.email").isEqualTo(updatedEmployeeDto.getEmail());

    }
}