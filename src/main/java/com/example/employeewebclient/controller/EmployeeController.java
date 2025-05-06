package com.example.employeewebclient.controller;

import com.example.employeewebclient.model.Employee;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Controller
public class EmployeeController {

    @GetMapping("/")
    public String getEmployees(Model model) {
        List<Employee> employees = fetchEmployeesFromServer();
        model.addAttribute("employees", employees);
        return "employees";
    }

    private List<Employee> fetchEmployeesFromServer() {
        List<Employee> list = new ArrayList<>();
        try {
            URL url = new URL("http://localhost:8080/employees");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            InputStreamReader reader = new InputStreamReader(connection.getInputStream());
            StringBuilder response = new StringBuilder();
            int i;
            while ((i = reader.read()) != -1) {
                response.append((char) i);
            }

            JSONArray employees = new JSONArray(response.toString());
            for (int j = 0; j < employees.length(); j++) {
                JSONObject emp = employees.getJSONObject(j);
                Employee e = new Employee();
                e.setId(emp.getLong("id"));
                e.setName(emp.getString("name"));
                e.setPosition(emp.getString("position"));
                e.setSalary(emp.getDouble("salary"));
                list.add(e);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
