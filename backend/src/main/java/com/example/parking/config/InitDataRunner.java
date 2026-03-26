package com.example.parking.config;

import com.example.parking.entity.RepairWorkerProfile;
import com.example.parking.entity.SysUser;
import com.example.parking.model.UserRole;
import com.example.parking.repository.RepairWorkerProfileRepository;
import com.example.parking.repository.SysUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class InitDataRunner {

    @Bean
    public CommandLineRunner initData(SysUserRepository sysUserRepository,
                                       RepairWorkerProfileRepository workerProfileRepository,
                                       PasswordEncoder passwordEncoder) {
        return args -> {
            if (!sysUserRepository.findByUsername("admin").isPresent()) {
                SysUser admin = new SysUser();
                admin.setUsername("admin");
                admin.setPasswordHash(passwordEncoder.encode("admin123"));
                admin.setRole(UserRole.ADMIN);
                admin.setDisplayName("管理员");
                sysUserRepository.save(admin);
            }

            if (!sysUserRepository.findByUsername("repair").isPresent()) {
                SysUser workerUser = new SysUser();
                workerUser.setUsername("repair");
                workerUser.setPasswordHash(passwordEncoder.encode("repair123"));
                workerUser.setRole(UserRole.REPAIR);
                workerUser.setDisplayName("维修人员");
                SysUser saved = sysUserRepository.save(workerUser);

                workerProfileRepository.findByUserId(saved.getId()).ifPresent(exist -> {
                    // 已存在则不处理
                });

                if (!workerProfileRepository.findByUserId(saved.getId()).isPresent()) {
                    RepairWorkerProfile profile = new RepairWorkerProfile();
                    profile.setUserId(saved.getId());
                    profile.setWorkerName("维修人员");
                    profile.setPhone("13800000000");
                    workerProfileRepository.save(profile);
                }
            }
        };
    }
}

