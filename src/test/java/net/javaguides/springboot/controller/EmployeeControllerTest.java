package net.javaguides.springboot.controller;

import java.util.List;
import net.javaguides.springboot.dto.EmployeeDto;
import net.javaguides.springboot.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = EmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @MockitoBean
    EmployeeService employeeService;

    @Test
    void givenEmployeeObject_whenSaveEmployee_thenReturnSavedEmployee() {

        // given - precondition or setup
        EmployeeDto employeeDto = EmployeeDto.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .build();

        BDDMockito.given(employeeService.saveEmployee(any(EmployeeDto.class)))
                .willReturn(Mono.just(employeeDto));

        // when - action or the behaviour that we are going to test
        WebTestClient.ResponseSpec response  = webTestClient.post().uri("/api/employees")
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(employeeDto), EmployeeDto.class)
                .exchange();

        // then - verify the output
        response.expectStatus().isCreated()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.firstName").isEqualTo(employeeDto.getFirstName())
                .jsonPath("$.lastName").isEqualTo(employeeDto.getLastName())
                .jsonPath("$.email").isEqualTo(employeeDto.getEmail());
    }

    @Test
    void givenEmployeeId_whenGetEmployee_thenReturnEmployeeObject() {

        // given - precondition or setup
        String employeeId = "6ac4f03726c4da05a9c36cf8";
        EmployeeDto employeeDto = EmployeeDto.builder()
                .id(employeeId)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .build();

        BDDMockito.given(employeeService.getEmployeeById(employeeId))
                .willReturn(Mono.just(employeeDto));

        // when - action or the behaviour that we are going to test
        WebTestClient.ResponseSpec response = webTestClient.get()
                .uri("/api/employees/{id}", employeeId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange();

        // then - verify the output
        response.expectStatus().isOk()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.firstName").isEqualTo(employeeDto.getFirstName())
                .jsonPath("$.lastName").isEqualTo(employeeDto.getLastName())
                .jsonPath("$.email").isEqualTo(employeeDto.getEmail());
    }

    @Test
    void givenListOfEmployees_whenGetAllEmployees_thenReturnEmployeeList() {

        // given - precondition or setup
        List<EmployeeDto> employeeDtoList = List.of(
                EmployeeDto.builder()
                        .firstName("John")
                        .lastName("Doe")
                        .email("john.doe@example.com")
                        .build(),
                EmployeeDto.builder()
                        .firstName("Jane")
                        .lastName("Smith")
                        .email("jane.smith@example.com")
                        .build()
        );

        Flux<EmployeeDto> employeeDtoFlux = Flux.fromIterable(employeeDtoList);
        BDDMockito.given(employeeService.getAllEmployees())
                .willReturn(employeeDtoFlux);

        // when - action or the behaviour that we are going to test
        WebTestClient.ResponseSpec response = webTestClient.get()
                .uri("/api/employees")
                .accept(MediaType.APPLICATION_JSON)
                .exchange();

        // then - verify the output
        response.expectStatus().isOk()
                .expectBodyList(EmployeeDto.class)
                .consumeWith(System.out::println)
                .hasSize(employeeDtoList.size());

    }
}