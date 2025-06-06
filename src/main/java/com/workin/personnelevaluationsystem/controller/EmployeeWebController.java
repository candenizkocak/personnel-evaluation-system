package com.workin.personnelevaluationsystem.controller;

import com.workin.personnelevaluationsystem.dto.EmployeeCreateDTO;
import com.workin.personnelevaluationsystem.dto.EmployeeResponseDTO;
import com.workin.personnelevaluationsystem.dto.PositionDTO; // Need Position DTO for dropdowns
import com.workin.personnelevaluationsystem.service.EmployeeService;
import com.workin.personnelevaluationsystem.service.PositionService; // Need Position Service for dropdowns
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/employees") // Base URL for Employee UI pages
public class EmployeeWebController {

    private final EmployeeService employeeService;
    private final PositionService positionService; // For fetching positions for dropdown

    @Autowired
    public EmployeeWebController(EmployeeService employeeService, PositionService positionService) {
        this.employeeService = employeeService;
        this.positionService = positionService;
    }

    // Helper to prepare model for employee form (reusable for new and edit)
    private void prepareEmployeeFormModel(Model model, EmployeeCreateDTO employeeDTO, boolean isEdit) {
        model.addAttribute("employee", employeeDTO);
        model.addAttribute("isEdit", isEdit);
        model.addAttribute("pageTitle", isEdit ? "Edit Employee" : "Add New Employee");

        // Fetch all positions for the dropdown
        List<PositionDTO> positions = positionService.getAllPositions();
        model.addAttribute("positions", positions);

        // Fetch all employees for the manager dropdown, exclude current employee if editing
        List<EmployeeResponseDTO> allEmployees = employeeService.getAllEmployees();
        List<EmployeeResponseDTO> potentialManagers = allEmployees.stream()
                .filter(emp -> !isEdit || !emp.getEmployeeID().equals(employeeDTO.getEmployeeID()))
                .collect(Collectors.toList());
        model.addAttribute("potentialManagers", potentialManagers);
    }

    // Display list of employees
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'HR_SPECIALIST', 'MANAGER')") // Admins, HR, Managers can view employees
    public String listEmployees(Model model) {
        List<EmployeeResponseDTO> employees = employeeService.getAllEmployees();
        model.addAttribute("employees", employees);
        model.addAttribute("pageTitle", "Employees List");
        return "employees/list"; // Resolves to /WEB-INF/views/employees/list.jsp
    }

    // Show form for adding a new employee
    @GetMapping("/new")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR_SPECIALIST')") // Only ADMIN or HR_SPECIALIST can add employees
    public String showAddForm(Model model) {
        EmployeeCreateDTO employeeDTO = (EmployeeCreateDTO) model.getAttribute("employee");
        if (employeeDTO == null) {
            employeeDTO = new EmployeeCreateDTO(); // Provide empty DTO if not present from redirect
        }
        prepareEmployeeFormModel(model, employeeDTO, false);
        return "employees/form"; // Resolves to /WEB-INF/views/employees/form.jsp
    }

    // Show form for editing an existing employee
    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR_SPECIALIST')") // Only ADMIN or HR_SPECIALIST can edit employees
    public String showEditForm(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        EmployeeCreateDTO employeeDTO = (EmployeeCreateDTO) model.getAttribute("employee");
        if (employeeDTO == null || !employeeDTO.getEmployeeID().equals(id)) { // If not coming from a redirect with errors for this specific ID
            Optional<EmployeeResponseDTO> existingEmployee = employeeService.getEmployeeById(id);
            if (existingEmployee.isPresent()) {
                // Convert ResponseDTO to CreateDTO for the form
                employeeDTO = EmployeeCreateDTO.builder()
                        .employeeID(existingEmployee.get().getEmployeeID())
                        .firstName(existingEmployee.get().getFirstName())
                        .lastName(existingEmployee.get().getLastName())
                        .email(existingEmployee.get().getEmail())
                        .phone(existingEmployee.get().getPhone())
                        .hireDate(existingEmployee.get().getHireDate())
                        .positionID(existingEmployee.get().getPositionID())
                        .managerID(existingEmployee.get().getManagerID())
                        .isActive(existingEmployee.get().getIsActive())
                        .build();
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Employee not found!");
                return "redirect:/employees";
            }
        }
        prepareEmployeeFormModel(model, employeeDTO, true);
        return "employees/form";
    }


    // Handle submission of add/edit form
    @PostMapping("/save")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR_SPECIALIST')") // Only ADMIN or HR_SPECIALIST can save employees
    public String saveEmployee(@Valid EmployeeCreateDTO employeeDTO,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Validation errors occurred. Please check the form.");
            redirectAttributes.addFlashAttribute("employee", employeeDTO);
            redirectAttributes.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + "employee", bindingResult);
            return (employeeDTO.getEmployeeID() != null) ? "redirect:/employees/edit/" + employeeDTO.getEmployeeID() : "redirect:/employees/new";
        }

        try {
            if (employeeDTO.getEmployeeID() == null) { // New employee
                employeeService.createEmployee(employeeDTO);
                redirectAttributes.addFlashAttribute("successMessage", "Employee created successfully!");
            } else { // Existing employee
                employeeService.updateEmployee(employeeDTO.getEmployeeID(), employeeDTO);
                redirectAttributes.addFlashAttribute("successMessage", "Employee updated successfully!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error saving employee: " + e.getMessage());
            // Pass back DTO and errors to repopulate form after service exception
            redirectAttributes.addFlashAttribute("employee", employeeDTO);
            redirectAttributes.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + "employee", bindingResult);
            return (employeeDTO.getEmployeeID() != null) ? "redirect:/employees/edit/" + employeeDTO.getEmployeeID() : "redirect:/employees/new";
        }

        return "redirect:/employees";
    }

    // Handle deletion of an employee
    @GetMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR_SPECIALIST')") // Only ADMIN or HR_SPECIALIST can delete employees
    public String deleteEmployee(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            employeeService.deleteEmployee(id);
            redirectAttributes.addFlashAttribute("successMessage", "Employee deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting employee: " + e.getMessage());
        }
        return "redirect:/employees";
    }
}