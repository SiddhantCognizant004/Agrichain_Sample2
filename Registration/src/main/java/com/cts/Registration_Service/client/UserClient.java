package com.cts.Registration_Service.client;
import com.cts.Registration_Service.dto.response.UserResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service")
public interface UserClient {
    @GetMapping("/users/{id}")
    UserResponseDTO fetchUserById(@PathVariable("id") Long id);
}