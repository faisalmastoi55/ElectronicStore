package com.electronic.store;

import com.electronic.store.entities.Role;
import com.electronic.store.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.UUID;


@SpringBootApplication
public class ElectronicStoreApplication implements CommandLineRunner {

	@Autowired
	private RoleRepository repository;

	@Value("${normal.role.id}")
	private String role_normal_id;

	@Value("${admin.role.id}")
	private String role_admin_id;

	public static void main(String[] args) {
		SpringApplication.run(ElectronicStoreApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {

		try {
			Role roleAdmin = Role.builder()
					.roleId(role_admin_id)
					.roleName("ROLE_ADMIN")
					.build();

			Role roleNormal = Role.builder()
					.roleId(role_normal_id)
					.roleName("ROLE_NORMAL")
					.build();

			repository.save(roleAdmin);
			repository.save(roleNormal);

		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
