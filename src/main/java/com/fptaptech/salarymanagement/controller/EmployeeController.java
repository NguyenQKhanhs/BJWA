package com.fptaptech.salarymanagement.controller;

import com.fptaptech.salarymanagement.entity.Employee;
import com.fptaptech.salarymanagement.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    // Hiển thị danh sách nhân viên
    @GetMapping
    public String listEmployees(Model model, @RequestParam(value = "errorMessage", required = false) String errorMessage) {
        model.addAttribute("employees", employeeService.getAllEmployees());
        if (errorMessage != null) {
            model.addAttribute("errorMessage", errorMessage);
        }
        return "employees";
    }

    // Hiển thị form thêm nhân viên
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("employee", new Employee());
        return "add-employee";
    }

    // Lưu nhân viên mới
    @PostMapping("/save")
    public String saveEmployee(@ModelAttribute Employee employee) {
        employeeService.saveEmployee(employee);
        return "redirect:/employees";
    }

    // Hiển thị form chỉnh sửa nhân viên
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Employee employee = employeeService.getEmployeeById(id);
        if (employee != null) {
            model.addAttribute("employee", employee);
            return "edit-employee";
        }
        return "redirect:/employees?errorMessage=Không tìm thấy nhân viên với ID " + id;
    }

    // Cập nhật thông tin nhân viên
    @PostMapping("/update")
    public String updateEmployee(@ModelAttribute Employee employee) {
        employeeService.saveEmployee(employee);
        return "redirect:/employees";
    }

    // Xóa nhân viên
    @GetMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable Long id) {
        Employee employee = employeeService.getEmployeeById(id);
        if (employee != null) {
            employeeService.deleteEmployee(id);
            return "redirect:/employees";
        }
        return "redirect:/employees?errorMessage=Không tìm thấy nhân viên để xóa!";
    }

    // Tìm kiếm nhân viên
    @GetMapping("/search")
    public String searchEmployees(@RequestParam("keyword") String keyword, Model model) {
        List<Employee> result = employeeService.searchEmployees(keyword);
        if (result.isEmpty()) {
            model.addAttribute("employees", employeeService.getAllEmployees()); // Giữ nguyên danh sách nhân viên
            model.addAttribute("errorMessage", "Không tìm thấy nhân viên nào với từ khóa: " + keyword);
            return "employees";
        }
        model.addAttribute("employees", result);
        return "employees";
    }
}
