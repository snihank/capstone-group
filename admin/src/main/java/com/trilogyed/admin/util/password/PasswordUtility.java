package com.trilogyed.admin.util.password;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordUtility {
    public static void main(String[] args) {

        PasswordEncoder enc = new BCryptPasswordEncoder();

        //keeping the password simple but in real case it won't be like this
        String adminPassword= "admin";
        String managerPassword="manager";
        String teamLeadPassword ="team";
        String employeePassword="employee";



        String adminPW= enc.encode(adminPassword);
        String managerPW= enc.encode(managerPassword);
        String teamPW= enc.encode(teamLeadPassword);
        String employeePW= enc.encode(employeePassword);


        System.out.println("------------------------------------------------");
        System.out.println("------------------------------------------------");
        System.out.println("admin password " +adminPW);
        System.out.println("------------------------------------------------");
        System.out.println("------------------------------------------------");
        System.out.println("manager password " +managerPW);
        System.out.println("------------------------------------------------");
        System.out.println("------------------------------------------------");
        System.out.println("Team Lead password " +teamPW);
        System.out.println("------------------------------------------------");
        System.out.println("------------------------------------------------");
        System.out.println("Employee password " +employeePW);

    }
}
