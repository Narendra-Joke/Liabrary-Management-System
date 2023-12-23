package org.gfg.minor1.service;

import org.gfg.minor1.models.Admin;
import org.gfg.minor1.models.User;
import org.gfg.minor1.repository.AdminRepository;
import org.gfg.minor1.repository.UserRepository;
import org.gfg.minor1.request.CreateAdminRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${admin.authority}")
    private String adminAuthority;

    @Autowired
    private UserRepository userRepository;


    public Admin create(CreateAdminRequest createAdminRequest){
        Admin admin = adminRepository.findByContact(createAdminRequest.getContact());
        if(admin != null){
            return admin;
        }
        admin = createAdminRequest.to();
        User user = User.builder().
                contact(admin.getContact()).
                password(passwordEncoder.encode(createAdminRequest.getPassword())).
                authorities(adminAuthority).
                build();
        user = userRepository.save(user);

        admin.setUser(user);
        return adminRepository.save(admin);
    }

}
