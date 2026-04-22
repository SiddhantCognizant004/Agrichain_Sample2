package com.cts.Registration_Service;

import com.cts.Registration_Service.dao.UserRepo;
import com.cts.Registration_Service.entity.Users;
import com.cts.Registration_Service.enums.UserRole;
import com.cts.Registration_Service.enums.UserStatus;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class RegistrationServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(RegistrationServiceApplication.class, args);
		System.out.println("Worked - Service Started Successfully");
	}

//	 //2. Added PasswordEncoder as a parameter here
//	@Bean
//	CommandLineRunner addUsers(UserRepo userRepo, PasswordEncoder passwordEncoder) {
//		return args -> {
//			// 1. Farmer
//			Users farmer = new Users();
//			farmer.setName("Farmer User");
//			farmer.setEmail("farmerUser@example.com");
//			farmer.setPhone("1231231234");
//			farmer.setRole(UserRole.FARMER);
//			farmer.setStatus(UserStatus.ACTIVE);
//			farmer.setPassword(passwordEncoder.encode("Farmer@123")); // Now it works!
//			userRepo.save(farmer);
//
//			// 2. Trader
//			Users trader = new Users();
//			trader.setName("Trader User");
//			trader.setEmail("traderUser@example.com");
//			trader.setPhone("3453453456");
//			trader.setRole(UserRole.TRADER);
//			trader.setStatus(UserStatus.ACTIVE);
//			trader.setPassword(passwordEncoder.encode("Trader@123"));
//			userRepo.save(trader);
//
//			// 3. Officer
//			Users officer = new Users();
//			officer.setName("Officer User");
//			officer.setEmail("officerUser@example.com");
//			officer.setPhone("4564564567");
//			officer.setRole(UserRole.OFFICER);
//			officer.setStatus(UserStatus.ACTIVE);
//			officer.setPassword(passwordEncoder.encode("Officer@123"));
//			userRepo.save(officer);
//
//			// 4. Manager
//			Users manager = new Users();
//			manager.setName("Manager User");
//			manager.setEmail("managerUser@example.com");
//			manager.setPhone("5675675678");
//			manager.setRole(UserRole.MANAGER);
//			manager.setStatus(UserStatus.ACTIVE);
//			manager.setPassword(passwordEncoder.encode("Manager@123"));
//			userRepo.save(manager);
//
//			// 5. Admin
//			Users admin = new Users();
//			admin.setName("Admin User");
//			admin.setEmail("adminUser@example.com");
//			admin.setPhone("2342342345");
//			admin.setRole(UserRole.ADMIN);
//			admin.setStatus(UserStatus.ACTIVE);
//			admin.setPassword(passwordEncoder.encode("Admin@123"));
//			userRepo.save(admin);
//
//			// 6. Compliance
//			Users compliance = new Users();
//			compliance.setName("Compliance User");
//			compliance.setEmail("complianceUser@example.com");
//			compliance.setPhone("6786786789");
//			compliance.setRole(UserRole.COMPLIANCE);
//			compliance.setStatus(UserStatus.ACTIVE);
//			compliance.setPassword(passwordEncoder.encode("Compliance@123"));
//			userRepo.save(compliance);
//
//			// 7. Auditor
//			Users auditor = new Users();
//			auditor.setName("Auditor User");
//			auditor.setEmail("auditorUser@example.com");
//			auditor.setPhone("7897897890");
//			auditor.setRole(UserRole.AUDITOR);
//			auditor.setStatus(UserStatus.ACTIVE);
//			auditor.setPassword(passwordEncoder.encode("Auditor@123"));
//			userRepo.save(auditor);
//
//			System.out.println("Added All Users by Hardcoded Code!");
//		};
//	}
}